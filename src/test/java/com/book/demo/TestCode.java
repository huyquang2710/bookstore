package com.book.demo;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TestCode {
	public static void main(String[] args) {
		String UUIDShow = UUID.randomUUID().toString();
		System.out.println(UUIDShow);
	}

//	private static Date calculateExpiryDate(final int expiryTimeInMinutes) {
//		final Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(new Date().getTime());
//		calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
//		return new Date(calendar.getTime().getTime());
//	}
}
