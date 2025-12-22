# 数据库课程在线培训考试系统

## 1. 环境配置要求

- MYSQL: 8.0(不宜过高，否则与远程服务器mysql配置不匹配)
- JAVA: jdk17(不宜过低，选稳定运行版本)
- mvn: 根据JAVA版本自动匹配
  ```
  brew install maven
  ```

## 2. 远程数据库连接

- 终端运行（参考 `.env.example`）
  ```
  mysql -h {DB_HOST} P {DB_PORT} -u {DB_USER} -p {DB_NAME}
  ```
- 查看数据库
  ```
  show tables;
  ```
- 查询特定表结构
  ```
  DESCRIBE sys_depart;
  ```
- 查看特定表内容
  ```
  SELECT * FROM sys_depart LIMIT 100;
  ```

## 3. 运行项目

- 配置环境变量（参考 `.env.example`）
- 运行后端
  ```
  cd exam-api/
  mvn spring-boot:run
  ```
- 运行前端
  ```
  cd exam-vue/
  npm install
  npm run dev
  ```
