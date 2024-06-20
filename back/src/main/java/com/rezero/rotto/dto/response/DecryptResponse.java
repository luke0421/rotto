package com.rezero.rotto.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DecryptResponse {

    private String phoneNum;
    private String juminNo;

}
