package io.junhyunny.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Profile {

    private String realName;
    private String realNameNormalized;
    private String displayName;
    private String displayNameNormalized;
}
