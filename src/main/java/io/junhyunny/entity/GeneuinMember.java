package io.junhyunny.entity;

import io.junhyunny.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneuinMember {

    private String name;
    private String realName;
    private Profile profile;

    public GeneuinMember() {
        profile = new Profile();
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
