package top.zspaces.lightlyim.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zspaces.lightlyim.mapper.UsersMapper;

@RestController
public class index {

    private final UsersMapper UM;
    @Autowired
    public index(UsersMapper usersMapper) {
        UM = usersMapper;
    }

    @GetMapping("/")
    public ResponseEntity<JSONArray> indexView() {
        return ResponseEntity.ok(JSONArray.from(UM.ListAll()));
    }
}
