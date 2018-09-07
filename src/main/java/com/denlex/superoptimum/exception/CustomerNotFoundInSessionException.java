package com.denlex.superoptimum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Shishkov A.V. on 06.09.18.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Покупатель не найден в сессии")
public class CustomerNotFoundInSessionException extends Exception {
}
