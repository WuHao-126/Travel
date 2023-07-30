package com.Travel.vo.param;

import com.Travel.dao.pojo.User;
import lombok.Data;

@Data
public class DeleteBlogParam {
    Integer blogId;
    Integer userId;
}
