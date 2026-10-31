# BTVN09 — Pharmacy Service / Spring Cloud Config

Java 17, Maven, Spring Boot 3.5.7 và Spring Cloud 2025.0.0.

Ứng dụng thực hiện yêu cầu trong đề: Config Server đọc cấu hình từ Git;
`pharmacy-service` nhận thông số database và thông tin chi nhánh, dùng `@Value`
để in tên chi nhánh/hotline khi khởi động và mở kết nối thật tới database.

## Cấu trúc

```text
config-server/       Config Server, cổng 8888
pharmacy-service/    Config Client, cổng 8080
config-repo/         File cấu hình mẫu được đưa lên Git cùng bài tập
scripts/            Script build/chạy với JDK 17
.local/config-repo/  Repository Git thật, nhánh main, được tạo tự động khi chạy
.local/data/         Database H2 lưu trên đĩa
```

Repository cấu hình local có commit riêng và được Config Server đọc bằng Git backend
(không dùng native/file backend). `.local/` được bỏ qua bởi `.gitignore`;
file mẫu trong `config-repo/` vẫn được đưa lên Git. Không cần tài khoản GitHub để chạy demo.

## Chạy ứng dụng

Cần JDK **17**, Maven **3.6.3+**, Git và kết nối mạng để tải dependency lần đầu.
Trên macOS, các script tự chọn JDK 17 đã cài. Với Linux/Windows Git Bash,
đặt `JAVA_HOME` trỏ tới JDK 17 trước khi chạy.

Tại thư mục gốc dự án:

```bash
bash scripts/build.sh
```

Terminal 1 — tạo repository cấu hình và khởi động Config Server:

```bash
bash scripts/run-config-server.sh
```

Đợi log `Started ConfigServerApplication`, rồi mở terminal 2:

```bash
bash scripts/run-pharmacy-service.sh
```

Log client mong đợi:

```text
Database connected: H2
Chi nhanh: Nha Thuoc So 1 | Hotline: 1900xxxx
```

Client bắt buộc lấy cấu hình từ Config Server. Nếu server không chạy hoặc database
không kết nối được, client sẽ báo lỗi khi khởi động. H2 được chọn cho demo để không
cần cài thêm database; dữ liệu được lưu tại `.local/data/`.

## Kiểm tra kết quả

```bash
# Cấu hình lấy từ Git; version trong JSON là mã commit Git
curl http://localhost:8888/pharmacy-service/default/main

# Thông tin nhận bằng @Value
curl http://localhost:8080/api/branch
# {"branchName":"Nha Thuoc So 1","hotline":"1900xxxx"}

# Kiểm tra sức khỏe ứng dụng, bao gồm kết nối database của client
curl http://localhost:8080/actuator/health
# {"status":"UP"}
```

## Thay đổi cấu hình

Sau lần chạy đầu, sửa `.local/config-repo/pharmacy-service.properties`, rồi commit:

```bash
git -C .local/config-repo add pharmacy-service.properties
git -C .local/config-repo -c user.name='Config Demo' \
  -c user.email='config-demo@example.invalid' commit -m 'Update branch configuration'
```

Khởi động lại **pharmacy-service** để nhận giá trị mới. Bài này không cấu hình
refresh động. Script khởi tạo không ghi đè file cấu hình local đã tồn tại.
Nếu muốn thay mẫu được nộp lên Git, sửa cả `config-repo/pharmacy-service.properties`.

## Dùng repository Git từ xa

Tạo repository trên GitHub/GitLab, đưa file `pharmacy-service.properties` từ
`config-repo/` vào thư mục gốc repository, commit lên nhánh `main`. Sau đó chạy:

```bash
CONFIG_GIT_URI=https://github.com/<tai-khoan>/<config-repo>.git \
  bash scripts/run-config-server.sh
```

Với repository private, truyền thông tin xác thực qua biến môi trường
`SPRING_CLOUD_CONFIG_SERVER_GIT_USERNAME` và `SPRING_CLOUD_CONFIG_SERVER_GIT_PASSWORD`.
Không commit mật khẩu/token thật. Cấu hình trong bài chỉ chứa tài khoản H2 demo.

Các biến có thể tùy chỉnh: `CONFIG_GIT_URI`, `CONFIG_GIT_LABEL` (mặc định `main`),
`CONFIG_SERVER_PORT` (8888), `CONFIG_SERVER_URL` (`http://localhost:8888`),
`PHARMACY_PORT` (8080). Khi đổi cổng server, đổi cả `CONFIG_SERVER_URL` cho client.

## IntelliJ IDEA

Mở `pom.xml` ở thư mục gốc dưới dạng Maven project. Chọn Project SDK và
Maven Runner JRE là **JDK 17**. Build cũng kiểm tra JDK 17 bằng Maven Enforcer.
Nếu chạy main class trực tiếp, chạy `bash scripts/init-config-repo.sh` trước,
đặt working directory của hai Run Configuration là thư mục gốc `btvn09`,
và khởi động `ConfigServerApplication` trước `PharmacyApplication`.

`.gitignore` bỏ qua output build, cấu hình IDE, log, database, repository demo
local và file môi trường. Source code, POM và file cấu hình mẫu vẫn được theo dõi.

Tài liệu: [Config Client](https://docs.spring.io/spring-cloud-config/reference/client.html),
[Spring Cloud 2025.0.0](https://spring.io/blog/2025/05/29/spring-cloud-2025-0-0-is-abvailable/).
# micro_btvn09_01
