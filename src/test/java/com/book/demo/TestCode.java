package com.book.demo;

import java.util.Calendar;
import java.util.Date;

public class TestCode {
	public static void main(String[] args) {
		Date cal = calculateExpiryDate(100);

		System.out.println(cal);
	}

	private static Date calculateExpiryDate(final int expiryTimeInMinutes) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(calendar.getTime().getTime());
	}
}
