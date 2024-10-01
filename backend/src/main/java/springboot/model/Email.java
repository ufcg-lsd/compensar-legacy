package springboot.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Email {

	@NotBlank(message = "Username cannot be blank")
	@Size(max = 100, message = "Username must be less than 100 characters")
	private final String username;

	@NotBlank(message = "Email cannot be blank")
	@jakarta.validation.constraints.Email(message = "Invalid email format")
	private final String email;

	@NotBlank(message = "Subject cannot be blank")
	@Size(max = 200, message = "Subject must be less than 200 characters")
	private final String subject;

	@NotBlank(message = "Message cannot be blank")
	private final String message;

	public Email(String username, String email, String subject, String message) {
		this.username = username;
		this.email = email;
		this.subject = subject;
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Email{".concat(
				"username='").concat(username).concat("', email='")
				.concat(email).concat("', subject='")
				.concat(subject).concat("', message='")
				.concat(message).concat("'}");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Email email1 = (Email) o;

		if (!username.equals(email1.username))
			return false;
		if (!email.equals(email1.email))
			return false;
		if (!subject.equals(email1.subject))
			return false;
		return message.equals(email1.message);
	}

	@Override
	public int hashCode() {
		int result = username.hashCode();
		result = 31 * result + email.hashCode();
		result = 31 * result + subject.hashCode();
		result = 31 * result + message.hashCode();
		return result;
	}
}
