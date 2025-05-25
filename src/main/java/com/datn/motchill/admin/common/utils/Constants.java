package com.datn.motchill.admin.common.utils;

public class Constants {
    private static final String MOVIES_SOURCE = "https://phim.nguonc.com/api/films";

    public static final String SUCCESS_CODE = "00";
    public static final String SUCCESS_DESC = "Thành công!";

    public static final String DATA_INVALID_CODE = "01";
    public static final String DATA_INVALID_CODE_DESC = "Dữ liệu không hợp lệ!";

    public static final String DATA_NOTFOUND_CODE = "02";
    public static final String DATA_NOTFOUND_DESC = "Dữ liệu không tồn tại!";

    public static final String INVALID_STATUS_CODE = "03";
    public static final String INVALID_STATUS_DESC = "Trạng thái không hợp lệ!";

    public static final String DATA_ALREADY_EXISTS_CODE = "04";
    public static final String DATA_ALREADY_EXISTS_DESC = "Dữ liệu đã tồn tại!";

    public static final String DATA_EXCEEDS_SPECIFIED_LENGTH = "05";
    public static final String DATA_EXCEEDS_SPECIFIED_LENGTH_DESC = "Dữ liệu vượt quá độ dài quy định!";

    public static final String MISSING_REQUIRED_DATA = "06";
    public static final String MISSING_REQUIRED_DATA_DESC = "Thiếu dữ liệu bắt buộc!";

    public static final String DATA_BINDING_ERROR = "07";
    public static final String DATA_BINDING_ERROR_DESC = "Lỗi ràng buộc dữ liệu!";

    public static final String NO_ACCESS_TO_RESOURCES = "09";
    public static final String NO_ACCESS_TO_RESOURCES_DESC = "Không có quyền truy cập vào tài nguyên!";

    public static final String ACCESS_DENIED_CODE = "43";
    public static final String ACCESS_DENIED_DESC = "Truy cập bị từ chối!";

    public static final String FAIL_CODE = "44";
    public static final String FAIL_DESC = "Thất bại!";

    public static final String S3_FAIL_CODE = "45";
    public static final String S3_FAIL_DESC = "Lỗi lấy file trên S3!";

    public static final String DELETE_DESC = "Xóa";
    public static final String RECORD_EXISTS_DESC = "Bản ghi đã tồn tại!";
    public static final String NGAY_HIEU_LUC_KHONG_DUOC_NHO_HON_NGAY_HIEN_TAI = "Ngày giờ hiệu lực không được nhỏ hơn ngày giờ hiện tại!";
    public static final String NGAY_HIEU_LUC_KHONG_DUOC_BANG_NGAY_HET_HIEU_LUC = "Ngày giờ hiệu lực không được bằng ngày giờ hết hiệu lực!";
    public static final String NGAY_HIEU_LUC_KHONG_DUOC_DE_TRONG = "Ngày hiệu lực không được để trống!";
    public static final String NGAY_HIEU_LUC_KHONG_DUNG_DINH_DANG = "Ngày hiệu lực không đúng định dạng dd/MM/yyyy hh:mm:ss!";
    public static final String NGAY_HET_HIEU_LUC_KHONG_DUNG_DINH_DANG = "Ngày hết hiệu lực không đúng định dạng dd/MM/yyyy hh:mm:ss!";
    public static final String NGAY_HET_HIEU_LUC_KHONG_DUOC_NHO_HON_NGAY_HIEN_TAI = "Ngày hết hiệu lực không được nhỏ hơn ngày hiện tại!";
    public static final String NGAY_HET_HIEU_LUC_KHONG_DUOC_DE_TRONG = "Ngày hết hiệu lực không được để trống!";
    public static final String NGAY_HET_HIEU_LUC_KHONG_DUOC_NHO_HON_NGAY_HIEU_LUC = "Ngày hết hiệu lực không được nhỏ hơn hoặc bằng ngày hiệu lực!";
    public static final String NGAY_HIEU_LUC_PHAI_BANG_NGAY_HIEN_TAI = "Ngày hiệu lực phải bằng ngày hiện tại";
    public static final String NGAY_HET_HIEU_LUC_KHONG_HOP_LE = "Ngày hiệu lực phải lớn hơn hoặc bằng ngày hiện tại và nhỏ hơn ngày hết hiệu lực!";

    public static final String DA_TON_TAI_NH_8_SO ="Đã tồn tại mã NH 8 số";
    public static final String DA_TON_TAI_NH_6_SO ="Đã tồn tại mã NH 6 số";

    public static final String ALL_RECORDS_INVALID ="Tất cả các bản ghi đều không hợp lệ";


    public static final String DANH_SACH_ID_KHONG_DUOC_DE_TRONG  = "Danh sách ID không được để trống!";
    public static final String KHONG_TIM_THAY_DU_LIEU_VOI_DANH_SACH_ID_DA_CHON = "Không tìm thấy dữ liệu với danh sách id đã chọn!";
    public static final String KHONG_TIM_THAY_DU_LIEU_VOI_ID_DA_CHON = "Không tìm thấy dữ liệu với id đã chọn!";
    public static final String TON_TAI_MOT_HOAC_NHIEU_THAM_SO_CO_TRANG_THAI_KHONG_THE_THUC_HIEN_THAO_TAC_NAY = "Tồn tại 1 hoặc nhiều tham số có trạng thái không thể thực hiện thao tác này!";
    public static final String ID_PHAI_DE_TRONG = "ID phải để trống!";
    public static final String ID_KHONG_DUOC_DE_TRONG = "ID không được để trống!";
    public static final String TRANG_THAI_THAM_SO_HIEN_TAI_KHONG_DUOC_THUC_HIEN_THAO_TAC_NAY = "Trạng thái tham số hiện tại không được thực hiện thao tác này!";
    public static final String ALL_RECORD_INVALID_EFFDATE = "Tất cả các bản ghi thao tác đều không hợp lệ 'ngày hiệu lực' hoặc 'ngày hết hiệu lực'";
    public static final String GUI_DUYET_THANH_CONG_PLACEHOLDER = "Gửi duyệt thành công %d/%d bản ghi";

    public static final String XOA_THANH_CONG_XXX_BAN_GHI = "Xóa thành công %d bản ghi";
    public static final String GUI_YC_XOA_THANH_CONG_XXX_BAN_GHI = "Gửi yêu cầu xóa thành công %d bản ghi.";
    public static final String XOA_THANH_CONG_XXX_BAN_GHI_GUI_YC_XOA_XXX_BAN_GHI = "Xóa thành công %d bản ghi, gửi yêu cầu xóa %d bản ghi.";
    public static final String PHE_DUYET_THANH_CONG_XXX_BAN_GHI = "Phê duyệt thành công %d bản ghi";
    public static final String PHE_DUYET_THANH_CONG_XXX_BAN_GHI_TREN_XXX_BAN_GHI = "Phê duyệt thành công %d/%d bản ghi";
    public static final String HUY_DUYET_THANH_CONG_XXX_BAN_GHI_TREN_XXX_BAN_GHI = "Hủy duyệt thành công %d/%d bản ghi";
    public static final String TU_CHOI_THANH_CONG_XXX_BAN_GHI_TREN_XXX_BAN_GHI = "Từ chối thành công %d/%d bản ghi";
    public static final String XOA_VA_PHE_DUYET_THANH_CONG_XXX_BAN_GHI = "Xóa thành công %d bản ghi, phê duyệt thành công %d/%d bản ghi";
    public static final String PHE_DUYET_THANH_CONG = "Phê duyệt thành công";
    public static final String KHONG_CO_BAN_GHI_NAO_DUOC_XU_LY = "Không có bản ghi nào được xử lý";
    public static final String CAP_PHE_DUYET_KHONG_HOP_LE = "Cấp phê duyệt không hợp lệ!";

    public static final String IDS_DESC_PLACEHOLDER = "Thành công: %d, thất bại: %d";

    // Bổ sung thêm


    public class DateFormatType {
        public static final String YYYY_MM_DD = "yyyy-MM-dd";
        public static final String DD_MM_YYYY = "dd/MM/yyyy";
        public static final String DD_MMM_YY = "dd-MMM-yy";
        public static final String DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss";
        public static final String DD_MM_YYYY_HH_MM_SS2 = "dd/MM/yyyy HH:mm:ss";

        public static final String DD_MM_YYYY_HH_MM = "dd.MM.yyyy HH:mm:ss";
        public static final String DDMMYYYY = "ddMMyyyy";
        public static final String YYYYMMDD = "yyyyMMdd";
        public static final String DD_MM_YYYY1 = "dd.MM.yyyy";
        public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        public static final String EFFECTIVE_DATE_PATTERN = "^(|\\d{2}/\\d{2}/\\d{4}|\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2})$";

        public static final String EFFECTIVE_DATE_MESSAGE = "Ngày hiệu lực phải có định dạng dd/MM/yyyy hoặc dd/MM/yyyy hh:mm:ss";
        public static final String END_EFFECTIVE_DATE_MESSAGE = "Ngày hết hiệu lực phải có định dạng dd/MM/yyyy hoặc dd/MM/yyyy hh:mm:ss";
    }

    public  class TimeFormat {
        public  static final String HH_MM_SS = "HH:mm:ss";
    }

    public class StringFormat {
        public static final String NO_SPECIAL_CHARS_AND_VIETNAMESE = "^[^\\^#\\|\\*\\@\\$\\`\\~\\!\\%\\&\\{\\}\\[\\]\\?\\<\\>\\\"',()/:;\\\\=\\sÀ-ỹ]*$";
        public static final String NO_SPECIAL_CHARS_AND_VIETNAMESE_AND_HAVE_SPACE = "^[^\\^#\\|\\*\\@\\$\\`\\~\\!\\%\\&\\{\\}\\[\\]\\?\\<\\>\\\"',()/:;\\\\=À-ỹ]*$";
        public static final String NO_SPECIAL_CHARS = "^[^\\^#\\|\\*\\@\\$\\`\\~\\!\\%\\&\\{\\}\\[\\]\\?\\<\\>\\\"',()/:;\\\\=\\s]*$";
        public static final String NO_SPECIAL_CHARS_AND_HAVE_SPACE = "^[^\\^#\\|\\*\\@\\$\\`\\~\\!\\%\\&\\{\\}\\[\\]\\?\\<\\>\\\"',()/:;\\\\=]*$";
        public static final String YES_NO_PATTERN = "Y|N";
    }


    public static class API_PATH {


        public static final String CONTEXT_PATH = "/api/v1/";

        public static class API_ADMIN_PATH {
            public static final String ADMIN_PATH = CONTEXT_PATH + "admin/";

            public static final String ADMIN_GENRE = ADMIN_PATH + "genres/";

            public static final String ADMIN_BANNER = ADMIN_PATH + "banners/";

            public static final String ADMIN_MOVIES = ADMIN_PATH + "movies/";
        }




    }

    public final class FunctionAllows {

        private FunctionAllows() {
        }

        public static final String SEARCH = "search";
        public static final String SAVE_DRAFT = "saveDraft";
        public static final String SAVE_AND_APPROVE = "saveAndApprove";
        public static final String SAVE_AND_SEND_APPROVAL = "saveAndSendApproval";
        public static final String UPDATE = "update";
        public static final String UPDATE_DRAFT = "updateDraft";
        public static final String UPDATE_AND_APPROVE = "updateAndApprove";
        public static final String UPDATE_AND_SEND_APPROVAL = "updateAndSendApproval";
        public static final String SEND_APPROVE = "sendApprove";
        public static final String APPROVE = "approve";
        public static final String CANCEL_APPROVAL = "cancelApproval";
        public static final String REJECT = "reject";
        public static final String DISPLAY = "display";
        public static final String EXPORT_EXCEL = "exportExcel";
        public static final String VIEW_DETAIL = "viewdetail";
        public static final String DELETE = "delete";
        public static final String HISTORY = "history";
        public static final String IMPORT_EXCEL = "importExcel";
        public static final String COPY = "copy";
        public static final String CREATE = "create";
        public static final String EDIT = "edit";
    }
}
