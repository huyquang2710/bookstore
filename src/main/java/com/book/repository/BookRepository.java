package com.book.repository;

import org.springframework.data.repository.CrudRepository;

import com.book.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

}
