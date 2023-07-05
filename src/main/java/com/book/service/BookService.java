package com.book.service;

import java.util.List;

import com.book.domain.Book;

public interface BookService {
	List<Book> findAll();

	Book findOne(Long id);
}
