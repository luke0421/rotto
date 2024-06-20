package com.rezero.rotto.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqRequest {

    private String title;
    private String content;

}
