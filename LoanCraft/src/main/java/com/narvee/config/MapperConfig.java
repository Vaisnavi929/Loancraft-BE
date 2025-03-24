package com.narvee.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

@org.springframework.context.annotation.Configuration
public class MapperConfig {

	public static ModelMapper getModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setFieldMatchingEnabled(true)
				.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE).setSkipNullEnabled(true);
		return modelMapper;
	}

}
