package com.example.funcart.HelperClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	private static Pattern pattern;

	private static final String EMAIL_REZEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ 
								"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String PHONE_REGEX = "^\\+?[0-9. ()-]{10,13}$";
	private static final String NAME_REGEX = "^[A-Za-z\\s]+$";
	private static final String PASSWORD_REZEX = "^[a-z0-9_-]{8,15}$";
	private static final String IMAGE_NAME_REZEX = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";;
	
	public static boolean emailValidate(final String hex) {
		
		pattern = null;
		pattern = Pattern.compile(EMAIL_REZEX);
		Matcher matcher = pattern.matcher(hex);
		
		return matcher.matches();

	}
	
	public static boolean phoneNumberValidate(final String hex){
		
		pattern = null;
		pattern = Pattern.compile(PHONE_REGEX);
		Matcher matcher = pattern.matcher(hex);
		
		return matcher.matches();
	}
	
	public static boolean nameValidate(final String hex){
		
		pattern = null;
		pattern = Pattern.compile(NAME_REGEX);
		Matcher matcher = pattern.matcher(hex);
		
		return matcher.matches();
	}
	
	public static boolean passwordValidate(final String hex) {
		
		pattern = null;
		pattern = Pattern.compile(PASSWORD_REZEX);
		Matcher matcher = pattern.matcher(hex);
		
		return matcher.matches();

	}
	
	public static boolean imageNameValidate(final String hex) {
		
		pattern = null;
		pattern = Pattern.compile(IMAGE_NAME_REZEX);
		Matcher matcher = pattern.matcher(hex);
		
		return matcher.matches();

	}
}