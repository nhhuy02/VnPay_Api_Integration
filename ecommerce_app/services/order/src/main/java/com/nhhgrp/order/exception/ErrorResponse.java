package com.nhhgrp.order.exception;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {

}
