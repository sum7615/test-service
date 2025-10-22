package com.spxam.test_service.util;

import java.time.LocalDateTime;
import java.util.List;

public class CommonUtil {
	
	public static boolean isEmptyString(String str) {
		return str==null || str.isEmpty();
	}
	public static void validateMandatory(String value, String fieldName, List<String> errors) {
		if (CommonUtil.isEmptyString(value)) {
			errors.add(fieldName + " is mandatory.");
		}
	}

	public static void validateMandatory(Long value, String fieldName, List<String> errors) {
		if (value==null || value<=0) {
			errors.add(fieldName + " is mandatory.");
		}
	}

	public static LocalDateTime getCurrentDateTime() {
		return LocalDateTime.now();
	}

}
