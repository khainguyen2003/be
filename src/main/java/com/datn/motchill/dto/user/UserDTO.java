package com.datn.motchill.dto.user;

import com.datn.motchill.dto.AbstractDTO;
import com.datn.motchill.enums.UserRoleEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends AbstractDTO<UserDTO> {

	@NotBlank(message = "Tên người dùng không được để trống")
	@Size(min = 3, max = 50, message = "Tên người dùng phải chứa từ 3 đến 50 ký tự")
	private String username;

	@NotBlank(message = "{blankPassword}")
	@Size(min = 3, max = 100, message = "{passwordSize}")
	private String password;

	@Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
	private String fullName;

	@Email(message = "Email không đúng định dạng")
	@Size(min = 6, max = 100, message = "{emailSize}")
	private String email;

	@Pattern(regexp = "^(0[0-9]{9})?$", message = "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0")
	private String phone;

	private Integer status;

	@Size(max = 255, message = "Đường dẫn avatar không được vượt quá 255 ký tự")
	private String avatar;

	private UserRoleEnum role;
}
