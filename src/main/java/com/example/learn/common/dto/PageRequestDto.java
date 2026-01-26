package com.example.learn.common.dto;

import com.example.learn.common.constants.GlobalConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageRequestDto {

    private int page = GlobalConstants.DEFAULT_PAGE;
    private int size = GlobalConstants.DEFAULT_SIZE;
    private String sortBy = GlobalConstants.DEFAULT_SORT_BY;
    private String sortDir = GlobalConstants.DEFAULT_SORT_DIR;
}
