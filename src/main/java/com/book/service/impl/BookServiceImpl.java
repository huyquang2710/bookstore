package com.book.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.domain.Book;
import com.book.repository.BookRepository;
import com.book.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Override
	public List<Book> findAll() {
		return (List<Book>) bookRepository.findAll();
	}

}
