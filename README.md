# LEARNLINGO (Dictionary App)

#### Table of contents
1. [Tác giả](#author)
2. [Giới thiệu](#introduction)
3. [Tính năng nổi bật](#features)
4. [UML Diagram](#uml)
5. [Cách sử dụng](#user-guide)
6. [Cài đặt](#installation)

# Tác giả <a name="author"></a>

DOM05
- Nguyễn Nhật Nam 23020557
- Trần Nhật Hưng 23020543
- Đặng Quốc Huy 23020539

# Giới thiệu <a name="introduction"></a>

* Ứng dụng quản lý sách thư viện giúp người dùng có thể mượn và trả được sách từ xa, đồng thời cũng giúp admin có thể quản lý được nguồn sách và lượng người dùng.
* Ứng dụng được viết bằng ngôn ngữ Java và sử dụng thư viện JavaFX để hỗ trợ tạo giao diện đồ họa.
* Ứng dụng còn tích hợp game “ai là triệu phú” hỏi về những kiến thức thú vị xoay quanh những cuốn sách, giúp người dùng giải trí và nâng cao kiến thức về sách.

<p align="center">
<img width="640" height="480" src="https://imgur.com/a/oCYuZ6i">
</p>

# Tính năng nổi bật <a name="features"></a>

Admin
* Mỗi khi ấn vào ô “thêm sách”, bạn hãy nhập sách cần tìm vào ô tìm kiếm, sau đó ấn search thì sẽ có danh sách hiện ra, ấn vào cuốn sách bạn muốn thêm và ấn “Add documents” là sách đó sẽ được thêm vào “kho sách”. 
* Sách của phần mềm rất đa dạng, vì nó sử dụng API của Google books (https://developers.google.com/books/docs/v1/using?hl=vi).
* Bạn có thể quan sát được người dùng đang mượn những cuốn sách nào, ngày mượn và ngày trả của chúng, trạng thái đang mượn hay đã quá hạn bằng cách ấn vào “lượt-mượn- trả”.
* Mật khẩu và tài khoản của cả người dùng và admin đều được liệt kê ở ô “tài khoản”.
User
* Bạn có thể thay đổi mật khẩu của mình.
* Bạn có thể mượn được sách ở ô “sách mượn”, sau đó hãy lựa chọn ngày mượn và ngày trả sao cho hợp lệ và ấn “mượn sách”. Sau đó sách vừa mượn sẽ xuất hiện trên bảng cột của phần trả sách, bạn có thể trả sách bằng cách ấn “trả sách”.

# UML Diagram <a name="uml"></a>

<p align="center">
<img width="640" height="480" src="https://imgur.com/a/fvidHj4">
</p>

# Cách sử dụng <a name="user-guide"></a>

* Video hướng dẫn sử dụng tại [Youtube](https://youtu.be/VqeeNMRmEaU?si=zVEVvyiYUU-L8GQJ).
- Trước tiên, bạn cần phải có tài khoản để có thể đăng nhập và sử dụng hệ thống của chúng tôi. Vậy nên việc cần làm đầu tiên là đăng ký tài khoản ở mục signup, nên chú ý việc ID bạn cần nhập mã sinh viên của mình, sau đó nhập chính xác các thông tin khác để tránh xảy ra sai lầm, rủi ro trong việc lưu trữ thông tin.
- Sau khi bạn đã có tài khoản thì tiến hành đăng nhập.
- Ở trong phần của người đọc thì có những chức năng chính là thông tin của người dùng, thao tác mượn trả sách, xem trong kho sách có những loại sách nào, xem trước thẻ thư viện của chính mình, chơi mini game củng cố kiến thức về sách và những bài nhạc để giúp bạn thư giãn, tạo sự thú vị khi sử dụng sách
- Nếu bạn muốn thay đổi trực tiếp mật khẩu trong user thì b chỉ cần nhập mật khẩu mới và xác nhận. Nhưng khi bạn quên mật khẩu và muốn login thành công thì điều tối thiểu là bạn phải nhớ tên đăng nhập của mình thì mới tạo mật khẩu mới được
- Khi sử dụng xong thì đăng xuất và thoát ra khỏi ứng dụng.
- Đối với Admin thì có mục tìm kiếm để tìm kiếm sách cần thêm vào thư viện
- Ở trong kho sách ta có thể kiểm soát được số lượng sách đang có và có thể xóa đi loại sách tùy ý.
- Ở mục tài khoản, danh sách các tài khoản của user và admin được hiển thị chi tiết các thông tin của họ. 
- Ở mục lượt mượn trả, admin có thể biết được tình trạng mượn sách của những người đang mượn sách, có 2 trạng thái “quá hạn” và “chưa trả” (chưa đến hạn trả).


# Cài đặt <a name="installation"></a>

* Cài đặt JDK 8 tại [đây](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html), có thể sử dụng Intellij để chạy chương trình viết bằng Java.
1. Clone project từ repository
2. Mở project trong IDE Intellij
3. Tìm đến main/LibraryManagement và run.
