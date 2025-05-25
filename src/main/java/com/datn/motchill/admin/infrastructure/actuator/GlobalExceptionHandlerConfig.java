package com.datn.motchill.admin.infrastructure.actuator;

import com.datn.motchill.admin.common.exceptions.BadRequestException;
import com.datn.motchill.admin.common.exceptions.CustomizeAccessDeniedException;
import com.datn.motchill.admin.common.exceptions.NotFoundException;
import com.datn.motchill.admin.presentation.dto.AdminBaseResponse;
import com.datn.motchill.admin.common.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandlerConfig {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
    	AdminBaseResponse response = new AdminBaseResponse();
        BindingResult bindingResult = ex.getBindingResult();

        // Lấy danh sách lỗi dưới dạng List<String> gọn gàng
        List<String> errors = bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> {
                    return fieldError.getDefaultMessage();
                })
                .collect(Collectors.toList());

        // Gán mã lỗi (status) tùy ý
        response.setStatus(Constants.MISSING_REQUIRED_DATA);

        // Gộp tất cả lỗi thành 1 chuỗi (hoặc có thể trả về mảng)
        // Ở đây, ta nối bằng dấu xuống dòng cho dễ đọc
        response.setMessage(String.join(" \n ", errors));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }
    
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AdminBaseResponse> handleConstraintViolationException(jakarta.validation.ConstraintViolationException ex) {
            List<String> errors = ex.getConstraintViolations()
                            .stream()
                            .map(violation -> violation.getMessage())
                            .collect(Collectors.toList());
            AdminBaseResponse errorResponse = new AdminBaseResponse().dataInvalid();
            errorResponse.setData(errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AdminBaseResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        AdminBaseResponse response = new AdminBaseResponse();
        response.setStatus(Constants.DATA_INVALID_CODE);
        response.setMessage(ex.getMessage());   
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<AdminBaseResponse> handleNotFoundException(NotFoundException ex) {
        AdminBaseResponse response = new AdminBaseResponse();
        response.setStatus(Constants.DATA_NOTFOUND_CODE);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<AdminBaseResponse> handleBadRequestException(BadRequestException ex) {
        AdminBaseResponse response = new AdminBaseResponse();
        response.setStatus(Constants.DATA_INVALID_CODE);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(CustomizeAccessDeniedException.class)
	public ResponseEntity<AdminBaseResponse> handleAccessDeniedException(
			CustomizeAccessDeniedException ex) {
		AdminBaseResponse response = new AdminBaseResponse();
		response.setStatus(Constants.ACCESS_DENIED_CODE);
		response.setMessage(ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}
    
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<AdminBaseResponse> handleNoResourceFoundException(org.springframework.web.servlet.resource.NoResourceFoundException ex) {
		AdminBaseResponse response = new AdminBaseResponse();
		response.setStatus(Constants.DATA_NOTFOUND_CODE);
		response.setMessage(ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<AdminBaseResponse> handleAccessDeniedException(Exception ex) {
//        AdminBaseResponse response = new AdminBaseResponse();
//        response.setStatus(Constants.ACCESS_DENIED_CODE);
//        response.setMessage(Constants.ACCESS_DENIED_DESC);
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
//    }
//
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AdminBaseResponse> handleException(Exception ex) {
    	String source = ex.getStackTrace()[0].getClassName();
        AdminBaseResponse response = new AdminBaseResponse();
        response.setStatus(Constants.FAIL_CODE);
        response.setMessage(source + ": " +ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }




}
