package com.techeazy.studentmanagement.response;



import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseStructure<T> {

	private String message;
	private T data;
	
	@SuppressWarnings("unchecked")
	public ResponseStructure(String message, List<? extends Object> dataList) {
	    this.message = message;
	    this.data = (T) dataList;
	
	}

}
