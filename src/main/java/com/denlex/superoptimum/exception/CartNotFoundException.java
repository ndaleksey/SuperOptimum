package com.denlex.superoptimum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Shishkov A.V. on 05.09.18.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Коризна покупателя не найдена")
public class CartNotFoundException extends Exception {
}
