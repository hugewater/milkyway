## 启动运行

```shell
docker compose up -d
```

当修改 docker 配置后，重新编译镜像：

```shell
docker compose up -d --no-deps --force-recreate --build --renew-anon-volumes
```

## 开发相关

**复制前端文件：**

```shell
export FRONTEND_VER=1.0
rm -rf nginx/dist/admin-${FRONTEND_VER} && \
cp -r ../../yudao-ui/yudao-ui-admin-vue3/dist-prod nginx/dist/admin-${FRONTEND_VER}
```



**清理运行日志及数据：**
```shell
# 清理日志和数据文件（自动确认删除）
yes | rm -rf backend/logs/* 
yes | rm -rf nginx/logs/* 
# 下面是删除数据库和缓存数据，请慎重执行
yes | rm -rf mysql/data/*
yes | rm -rf redis/data/*
```

## 服务器的宿主机端口映射

- Admin web: http://localhost:8080
- Api server: http://localhost:48080
- Mysql: localhost:3306, root/123456
- Redis: localhost:6379
