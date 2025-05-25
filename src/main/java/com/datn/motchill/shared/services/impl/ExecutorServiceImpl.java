package com.datn.motchill.shared.services.impl;

import com.datn.motchill.shared.annotation.ColumnMapping;
import com.datn.motchill.admin.common.utils.Constants;
import com.datn.motchill.shared.dto.ResponseProcedure;
import com.datn.motchill.shared.enums.BaseEnum;
import com.datn.motchill.shared.services.ExecutorService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExecutorServiceImpl implements ExecutorService {
    private final EntityManager entityManager;

    /**
     * Thực thi stored procedure Oracle trả về:
     * - Nếu dtoClass != null: 3 OUT: po_status, po_message, po_result (SYS_REFCURSOR) được ánh xạ thành List<T>
     * - Nếu dtoClass == null: 2 OUT: po_status, po_message, chỉ để biết kết quả.
     */
    @Override
    public <T> ResponseProcedure<T> execute(String procedureName, Map<String, Object> inParams, Class<T> dtoClass) {
        ResponseProcedure<T> response = new ResponseProcedure<>();
        List<T> resultData = new ArrayList<>();

        // Xác định số lượng OUT parameter: có result set hay không
        boolean hasResultSet = (dtoClass != null);
        int outParamsCount = hasResultSet ? 3 : 2;
        int inCount = (inParams != null ? inParams.size() : 0);
        int totalParams = inCount + outParamsCount;
        String callSql = buildCallSql(procedureName, totalParams);

        // Sử dụng Hibernate Session.doWork để tái sử dụng connection từ EntityManager
        Session session = entityManager.unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(java.sql.Connection connection) throws SQLException {
                try (CallableStatement cs = connection.prepareCall(callSql)) {
                    // Gán các tham số IN
                    setInParameters(cs, inParams);

                    // Đăng ký các tham số OUT theo thứ tự: po_status, po_message, (po_result nếu có)
                    int statusIdx = inCount + 1;
                    int messageIdx = inCount + 2;
                    cs.registerOutParameter(statusIdx, Types.VARCHAR);
                    cs.registerOutParameter(messageIdx, Types.VARCHAR);
                    if (hasResultSet) {
                        int resultIdx = inCount + 3;
                        cs.registerOutParameter(resultIdx, Types.REF_CURSOR);
                    }

                    cs.execute();

                    // Lấy giá trị OUT: status và message
                    String poStatus = cs.getString(statusIdx);
                    String poMessage = cs.getString(messageIdx);
                    response.setStatus(poStatus);
                    response.setMessage(poMessage);

                    // Nếu procedure trả về kết quả (SYS_REFCURSOR) thì ánh xạ kết quả thành danh sách DTO
                    if (Constants.SUCCESS_CODE.equals(poStatus) && hasResultSet) {
                        int resultIdx = inCount + 3;
                        List<T> list = extractResult(cs, resultIdx, dtoClass);
                        resultData.addAll(list);
                        response.setData(resultData);
                    } else {
                        response.setData(null);
                    }
                } catch (Exception e) {
                    throw new SQLException("Lỗi khi gọi thủ tục: " + e.getMessage(), e);
                }
            }
        });

        return response;
    }

    @Override
    public <T> List<T> executeNativeQuery(String sql, List<Object> params, Class<T> dtoClass){
        List<T> result = new ArrayList<>();

        // Lấy JDBC Connection qua Hibernate Session
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                // 1. Gán các tham số IN (nếu có)
                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        ps.setObject(i + 1, params.get(i));
                    }
                }
                // 2. Thực thi và duyệt ResultSet
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // 3. Ánh xạ mỗi row vào DTO
                        T dto = null;
                        try {
                            dto = mapRowToDto(rs, dtoClass);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        result.add(dto);
                    }
                }
            }
        });

        return result;
    }

    /**
     * Xây dựng câu lệnh gọi stored procedure theo dạng: { call PROCEDURE(?, ?, ...) }.
     *
     * @param procedureName Tên thủ tục cần gọi.
     * @param totalParams   Tổng số tham số (IN + OUT).
     * @return Câu lệnh SQL đã xây dựng.
     */
    private String buildCallSql(String procedureName, int totalParams) {
        validateProcedureName(procedureName);
        String placeholders = String.join(", ", Collections.nCopies(totalParams, "?"));
        return "{ call " + procedureName + "(" + placeholders + ") }";
    }

    /**
     * Kiểm tra tính hợp lệ của tên procedure để ngăn chặn SQL injection.
     * Chỉ cho phép các ký tự chữ số, dấu gạch dưới và dấu chấm.
     *
     * @param procedureName Tên procedure cần kiểm tra
     * @throws IllegalArgumentException nếu tên procedure không hợp lệ
     */
    private void validateProcedureName(String procedureName) {
        if (procedureName == null || procedureName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên procedure không được để trống");
        }
        if (!procedureName.matches("^[a-zA-Z0-9_.]+$")) {
            throw new IllegalArgumentException("Tên procedure không hợp lệ. Chỉ cho phép chữ số, dấu gạch dưới và dấu chấm");
        }
    }

    /**
     * Gán các tham số IN cho CallableStatement.
     *
     * @param cs       CallableStatement.
     * @param inParams Map chứa các tham số IN (theo thứ tự cần khớp với procedure).
     * @throws SQLException Nếu có lỗi trong quá trình gán.
     */
    private void setInParameters(CallableStatement cs, Map<String, Object> inParams) throws SQLException {
        if (inParams != null) {
            int index = 1;
            for (Map.Entry<String, Object> entry : inParams.entrySet()) {
                cs.setObject(index, entry.getValue());
                index++;
            }
        }
    }

    /**
     * Lấy kết quả từ tham số OUT (SYS_REFCURSOR) và ánh xạ thành danh sách DTO.
     *
     * @param cs          CallableStatement đã thực thi.
     * @param outParamIdx Vị trí tham số OUT chứa REF_CURSOR.
     * @param dtoClass    Lớp DTO để ánh xạ.
     * @param <T>         Kiểu của đối tượng DTO.
     * @return Danh sách DTO được ánh xạ.
     * @throws SQLException               Nếu có lỗi khi truy xuất ResultSet.
     * @throws InvocationTargetException  Nếu có lỗi khi khởi tạo đối tượng.
     * @throws InstantiationException     Nếu không thể tạo instance của dtoClass.
     * @throws IllegalAccessException     Nếu không thể truy cập constructor.
     * @throws NoSuchMethodException      Nếu không tìm thấy constructor phù hợp.
     */
    private <T> List<T> extractResult(CallableStatement cs, int outParamIdx, Class<T> dtoClass)
            throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        List<T> list = new ArrayList<>();
        try (ResultSet rs = (ResultSet) cs.getObject(outParamIdx)) {
            if(rs != null)
                while (rs.next()) {
                    T dto = mapRowToDto(rs, dtoClass);
                    list.add(dto);
                }
        }
        return list;
    }

    /**
     * Ánh xạ một hàng trong ResultSet thành đối tượng DTO dựa trên @ColumnMapping.
     *
     * @param rs       ResultSet chứa dữ liệu.
     * @param dtoClass Lớp DTO cần ánh xạ.
     * @param <T>      Kiểu của đối tượng DTO.
     * @return Đối tượng DTO sau khi ánh xạ.
     * @throws SQLException               Nếu có lỗi khi truy xuất dữ liệu.
     * @throws InvocationTargetException  Nếu có lỗi khi khởi tạo đối tượng.
     * @throws InstantiationException     Nếu không thể tạo instance của dtoClass.
     * @throws IllegalAccessException     Nếu không thể truy cập constructor.
     * @throws NoSuchMethodException      Nếu không tìm thấy constructor phù hợp.
     */
    private <T> T mapRowToDto(ResultSet rs, Class<T> dtoClass)
            throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        // 1. Tạo instance DTO
        T dtoInstance = dtoClass.getDeclaredConstructor().newInstance();

        // 2. Lấy metadata và ánh xạ tên cột -> field
        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();
        Map<String, Field> fieldMap = getMappedFields(dtoClass); // tên cột -> field

        // 3. Tìm extraColumns field
        Field extraField = getExtraColumnField(dtoClass);
        @SuppressWarnings("unchecked")
        Map<String, Object> extraMap = extraField == null ? null : (Map<String, Object>) extraField.get(dtoInstance);

        // 4. Duyệt từng cột theo index
        for (int i = 1; i <= colCount; i++) {
            String colName = md.getColumnLabel(i).toUpperCase();
            Field field = fieldMap.get(colName);

            if (field != null) {
                field.setAccessible(true);
                Object val = getColumnValue(rs, i, field.getType());
                field.set(dtoInstance, val);
            } else if (extraMap != null) {
                Object val = rs.getObject(i);
                extraMap.put(colName, val);
            }
        }

        return dtoInstance;
    }

    private List<String> getColumnNamesFromResultSet(ResultSetMetaData md, int colCount) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= colCount; i++) {
            columnNames.add(md.getColumnLabel(i).toUpperCase());
        }
        return columnNames;
    }

    private Map<String, Field> getMappedFields(Class<?> dtoClass) {
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : dtoClass.getDeclaredFields()) {
            ColumnMapping anno = field.getAnnotation(ColumnMapping.class);
            if (anno != null) {
                fieldMap.put(anno.value().toUpperCase(), field);
            }
        }
        return fieldMap;
    }

    private Field getExtraColumnField(Class<?> dtoClass) {
        for (Field field : dtoClass.getDeclaredFields()) {
            if (Map.class.isAssignableFrom(field.getType())
                    && field.getName().equals("extraColumns")) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }


    /**
     * Lấy giá trị của cột từ ResultSet dựa theo kiểu của field.
     *
     * @param rs         ResultSet chứa dữ liệu.
     * @param index index của cột.
     * @param fieldType  Kiểu của field.
     * @return Giá trị tương ứng với cột.
     * @throws SQLException Nếu có lỗi khi truy xuất dữ liệu.
     */
    private Object getColumnValue(ResultSet rs, int index, Class<?> fieldType) throws SQLException {
        Object value;
        if (fieldType == String.class) {
            value = rs.getString(index);
        } else if (fieldType == int.class || fieldType == Integer.class) {
            value = rs.getObject(index);
            if (value != null) value = ((Number) value).intValue();
        } else if (fieldType == long.class || fieldType == Long.class) {
            value = rs.getObject(index);
            if (value != null) value = ((Number) value).longValue();
        } else if (fieldType == double.class || fieldType == Double.class) {
            value = rs.getObject(index);
            if (value != null) value = ((Number) value).doubleValue();
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            value = rs.getObject(index);
            if (value != null) value = rs.getBoolean(index);
        } else if (fieldType == java.math.BigDecimal.class) {
            value = rs.getBigDecimal(index);
        } else if (fieldType == java.util.Date.class) {
            value = rs.getDate(index);
        } else if (fieldType.getSimpleName().equals("Timestamp")) {
            value = rs.getTimestamp(index);
        } else {
            value = rs.getObject(index);
        }

        if (fieldType.isEnum() && BaseEnum.class.isAssignableFrom(fieldType) && value != null) {
            return convertToEnum((Number) value, fieldType);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private <E extends Enum<E> & BaseEnum> E convertToEnum(Number value, Class<?> enumType) {
        int key = value.intValue();
        for (Object constant : enumType.getEnumConstants()) {
            BaseEnum baseEnum = (BaseEnum) constant;
            if (baseEnum.getKey().equals(key)) {
                return (E) constant;
            }
        }
        return null;
    }
}
