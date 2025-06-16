import java.util.*;

class User {
    private String userId;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private List<String> friendList;
    private List<String> blockedList;
    private Map<String, ChatRoom> chatRooms;
    
    public User(String userId, String password, String nickname, String email, String phone) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.friendList = new ArrayList<>();
        this.blockedList = new ArrayList<>();
        this.chatRooms = new HashMap<>();
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<String> getFriendList() { return friendList; }
    public List<String> getBlockedList() { return blockedList; }
    public Map<String, ChatRoom> getChatRooms() { return chatRooms; }
    
    // 친구 관련 메서드
    public void addFriend(String friendId) {
        if (!friendList.contains(friendId) && !blockedList.contains(friendId)) {
            friendList.add(friendId);
        }
    }
    
    public void removeFriend(String friendId) {
        friendList.remove(friendId);
    }
    
    public void blockUser(String userId) {
        if (!blockedList.contains(userId)) {
            blockedList.add(userId);
            friendList.remove(userId);
        }
    }
    
    public void unblockUser(String userId) {
        blockedList.remove(userId);
    }
    
    public boolean isFriend(String userId) {
        return friendList.contains(userId);
    }
    
    public boolean isBlocked(String userId) {
        return blockedList.contains(userId);
    }
}

// Message.java - 메시지 정보를 저장하는 클래스
class Message {
    private String messageId;
    private String senderId;
    private String content;
    private Date timestamp;
    private boolean isRead;
    private boolean isDeleted;
    private boolean isPinned;
    private boolean isBookmarked;
    private String replyToMessageId;
    
    public Message(String messageId, String senderId, String content) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = new Date();
        this.isRead = false;
        this.isDeleted = false;
        this.isPinned = false;
        this.isBookmarked = false;
    }
    
    // Getters and Setters
    public String getMessageId() { return messageId; }
    public String getSenderId() { return senderId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getTimestamp() { return timestamp; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { isPinned = pinned; }
    public boolean isBookmarked() { return isBookmarked; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }
    public String getReplyToMessageId() { return replyToMessageId; }
    public void setReplyToMessageId(String replyToMessageId) { this.replyToMessageId = replyToMessageId; }
}

// ChatRoom.java - 채팅방 정보를 저장하는 클래스
class ChatRoom {
    private String roomId;
    private String roomName;
    private List<String> participants;
    private List<Message> messages;
    private boolean isPinned;
    private Date lastMessageTime;
    private boolean isGroupChat;
    
    public ChatRoom(String roomId, String roomName, boolean isGroupChat) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.isGroupChat = isGroupChat;
        this.participants = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.isPinned = false;
        this.lastMessageTime = new Date();
    }
    
    // Getters and Setters
    public String getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public List<String> getParticipants() { return participants; }
    public List<Message> getMessages() { return messages; }
    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { isPinned = pinned; }
    public Date getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(Date lastMessageTime) { this.lastMessageTime = lastMessageTime; }
    public boolean isGroupChat() { return isGroupChat; }
    
    public void addParticipant(String userId) {
        if (!participants.contains(userId)) {
            participants.add(userId);
        }
    }
    
    public void removeParticipant(String userId) {
        participants.remove(userId);
    }
    
    public void addMessage(Message message) {
        messages.add(message);
        this.lastMessageTime = message.getTimestamp();
    }
    
    public int getUnreadCount(String userId) {
        int count = 0;
        for (Message msg : messages) {
            if (!msg.getSenderId().equals(userId) && !msg.isRead()) {
                count++;
            }
        }
        return count;
    }
}

// ChatApplication.java - 메인 애플리케이션 클래스
public class ChatApplication {
    private Map<String, User> users;
    private Map<String, ChatRoom> chatRooms;
    private User currentUser;
    private Scanner scanner;
    private int messageIdCounter;
    private int roomIdCounter;
    
    public ChatApplication() {
        users = new HashMap<>();
        chatRooms = new HashMap<>();
        scanner = new Scanner(System.in);
        messageIdCounter = 1;
        roomIdCounter = 1;
        
        // 테스트용 기본 계정들 생성
        createDefaultTestAccounts();
    }
    
    private void createDefaultTestAccounts() {
        // 테스트용 기본 계정 3개 생성
        User test1 = new User("test1", "0000", "test1", "test1@example.com", "010-1111-1111");
        User test2 = new User("test2", "0000", "test2", "test2@example.com", "010-2222-2222");
        User test3 = new User("test3", "0000", "test3", "test3@example.com", "010-3333-3333");
        
        users.put("test1", test1);
        users.put("test2", test2);
        users.put("test3", test3);
        
        // 테스트 계정들끼리 서로 친구 추가
        test1.addFriend("test2");
        test1.addFriend("test3");
        test2.addFriend("test1");
        test2.addFriend("test3");
        test3.addFriend("test1");
        test3.addFriend("test2");
        
        System.out.println("테스트용 계정이 생성되었습니다:");
        System.out.println("- test1 / 0000 (test1)");
        System.out.println("- test2 / 0000 (test2)");
        System.out.println("- test3 / 0000 (test3)");
        System.out.println();
    }
    
    public void start() {
        System.out.println("=== 채팅 애플리케이션에 오신 것을 환영합니다! ===");
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    private void showLoginMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    로그인 메뉴    ");
        System.out.println("-".repeat(50));
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 아이디 찾기");
        System.out.println("4. 비밀번호 찾기");
        System.out.println("5. 프로그램 종료");
        System.out.println("-".repeat(50));
        System.out.print("선택: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행문자 처리
            
            switch (choice) {
                case 1: login(); break;
                case 2: register(); break;
                case 3: findUserId(); break;
                case 4: findPassword(); break;
                case 5: 
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        } catch (Exception e) {
            System.out.println("❌ 숫자를 입력해주세요.");
            scanner.nextLine(); // 잘못된 입력 버퍼 비우기
        }
    }
    
    private void showMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    메인 메뉴    ");
        System.out.println("현재 사용자: " + currentUser.getNickname() + " (" + currentUser.getUserId() + ")");
        System.out.println("-".repeat(50));
        System.out.println("1. 채팅");
        System.out.println("2. 친구 관리");
        System.out.println("3. 프로필 관리");
        System.out.println("4. 로그아웃");
        System.out.println("-".repeat(50));
        System.out.print("선택: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1: chatMenu(); break;
                case 2: friendMenu(); break;
                case 3: profileMenu(); break;
                case 4: logout(); break;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        } catch (Exception e) {
            System.out.println("❌ 숫자를 입력해주세요.");
            scanner.nextLine(); // 잘못된 입력 버퍼 비우기
        }
    }
    
    private void register() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    회원가입     ");
        System.out.println("-".repeat(50));
        
        String userId = "";
        String password = "";
        String nickname = "";
        
        // 필수 항목: 아이디
        while (userId.trim().isEmpty()) {
            System.out.print("아이디 (필수): ");
            userId = scanner.nextLine().trim();
            
            if (userId.isEmpty()) {
                System.out.println("❌ 아이디는 필수 입력 항목입니다.");
                continue;
            }
            
            if (users.containsKey(userId)) {
                System.out.println("❌ 이미 존재하는 아이디입니다.");
                userId = "";
            }
        }
        
        // 필수 항목: 비밀번호
        while (password.trim().isEmpty()) {
            System.out.print("비밀번호 (필수): ");
            password = scanner.nextLine().trim();
            
            if (password.isEmpty()) {
                System.out.println("❌ 비밀번호는 필수 입력 항목입니다.");
            }
        }
        
        // 필수 항목: 닉네임
        while (nickname.trim().isEmpty()) {
            System.out.print("닉네임 (필수): ");
            nickname = scanner.nextLine().trim();
            
            if (nickname.isEmpty()) {
                System.out.println("❌ 닉네임은 필수 입력 항목입니다.");
            }
        }
        
        // 선택 항목: 이메일
        System.out.print("이메일 (선택): ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            email = "이메일 없음";
        }
        
        // 선택 항목: 전화번호
        System.out.print("전화번호 (선택): ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) {
            phone = "전화번호 없음";
        }
        
        User newUser = new User(userId, password, nickname, email, phone);
        
        // 새 사용자에게 기본 테스트 계정들을 친구로 추가
        addDefaultFriendsToNewUser(newUser);
        
        users.put(userId, newUser);
        
        System.out.println("\n");
        System.out.println("✅ 회원가입이 완료되었습니다!");
        System.out.println("✅ 기본 친구 3명이 자동으로 추가되었습니다. (test1, test2, test3)");
        System.out.println("-".repeat(50));
    }
    
    private void addDefaultFriendsToNewUser(User newUser) {
        // 테스트 계정이 아닌 경우에만 기본 친구 추가
        if (!newUser.getUserId().startsWith("test")) {
            // 테스트 계정들을 친구로 추가
            newUser.addFriend("test1");
            newUser.addFriend("test2");
            newUser.addFriend("test3");
            
            // 테스트 계정들의 친구 목록에도 새 사용자 추가
            if (users.containsKey("test1")) {
                users.get("test1").addFriend(newUser.getUserId());
            }
            if (users.containsKey("test2")) {
                users.get("test2").addFriend(newUser.getUserId());
            }
            if (users.containsKey("test3")) {
                users.get("test3").addFriend(newUser.getUserId());
            }
        }
    }
    
    private void login() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    로그인    ");
        System.out.println("-".repeat(50));
        System.out.print("아이디: ");
        String userId = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();
        
        User user = users.get(userId);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("\n로그인 성공! 환영합니다, " + user.getNickname() + "님!\n");
        } else {
            System.out.println("\n❌ 아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }
    
    private void findUserId() {
        System.out.println("\n=== 아이디 찾기 ===");
        System.out.print("이메일: ");
        String email = scanner.nextLine();
        
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                System.out.println("찾은 아이디: " + user.getUserId());
                return;
            }
        }
        System.out.println("해당 이메일로 가입된 계정을 찾을 수 없습니다.");
    }
    
    private void findPassword() {
        System.out.println("\n=== 비밀번호 찾기 ===");
        System.out.print("아이디: ");
        String userId = scanner.nextLine();
        System.out.print("이메일: ");
        String email = scanner.nextLine();
        
        User user = users.get(userId);
        if (user != null && user.getEmail().equals(email)) {
            System.out.println("임시 비밀번호가 이메일로 전송되었습니다.");
            System.out.println("현재 비밀번호: " + user.getPassword());
        } else {
            System.out.println("아이디 또는 이메일이 일치하지 않습니다.");
        }
    }
    
    private void logout() {
        currentUser = null;
        System.out.println("로그아웃되었습니다.");
    }
    
    private void friendMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("     친구 관리     ");
        System.out.println("-".repeat(50));
        System.out.println("1. 친구 목록 보기");
        System.out.println("2. 친구 추가");
        System.out.println("3. 친구 삭제");
        System.out.println("4. 사용자 차단");
        System.out.println("5. 차단 해제");
        System.out.println("6. 차단 목록 보기");
        System.out.println("7. 친구 검색");
        System.out.println("8. 메인 메뉴로");
        System.out.println("-".repeat(30));
        System.out.print("선택: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1: showFriendList(); break;
            case 2: addFriend(); break;
            case 3: removeFriend(); break;
            case 4: blockUser(); break;
            case 5: unblockUser(); break;
            case 6: showBlockedList(); break;
            case 7: searchFriend(); break;
            case 8: return;
            default:
                System.out.println("잘못된 선택입니다.");
        }
    }
    
    private void showFriendList() {
        System.out.println("\n=== 친구 목록 ===");
        List<String> friends = currentUser.getFriendList();
        if (friends.isEmpty()) {
            System.out.println("친구가 없습니다.");
        } else {
            for (String friendId : friends) {
                User friend = users.get(friendId);
                if (friend != null) {
                    System.out.println("- " + friend.getNickname() + " (" + friendId + ")");
                }
            }
        }
    }
    
    private void addFriend() {
        System.out.print("추가할 친구의 아이디: ");
        String friendId = scanner.nextLine();
        
        if (friendId.equals(currentUser.getUserId())) {
            System.out.println("자신을 친구로 추가할 수 없습니다.");
            return;
        }
        
        if (!users.containsKey(friendId)) {
            System.out.println("존재하지 않는 사용자입니다.");
            return;
        }
        
        if (currentUser.isFriend(friendId)) {
            System.out.println("이미 친구입니다.");
            return;
        }
        
        if (currentUser.isBlocked(friendId)) {
            System.out.println("차단된 사용자입니다. 차단을 해제한 후 추가해주세요.");
            return;
        }
        
        currentUser.addFriend(friendId);
        System.out.println("친구가 추가되었습니다!");
    }
    
    private void removeFriend() {
        System.out.print("삭제할 친구의 아이디: ");
        String friendId = scanner.nextLine();
        
        if (currentUser.isFriend(friendId)) {
            currentUser.removeFriend(friendId);
            System.out.println("친구가 삭제되었습니다.");
        } else {
            System.out.println("친구 목록에 없는 사용자입니다.");
        }
    }
    
    private void blockUser() {
        System.out.print("차단할 사용자의 아이디: ");
        String userId = scanner.nextLine();
        
        if (!users.containsKey(userId)) {
            System.out.println("존재하지 않는 사용자입니다.");
            return;
        }
        
        if (userId.equals(currentUser.getUserId())) {
            System.out.println("자신을 차단할 수 없습니다.");
            return;
        }
        
        currentUser.blockUser(userId);
        System.out.println("사용자가 차단되었습니다.");
    }
    
    private void unblockUser() {
        System.out.print("차단 해제할 사용자의 아이디: ");
        String userId = scanner.nextLine();
        
        if (currentUser.isBlocked(userId)) {
            currentUser.unblockUser(userId);
            System.out.println("차단이 해제되었습니다.");
        } else {
            System.out.println("차단 목록에 없는 사용자입니다.");
        }
    }
    
    private void showBlockedList() {
        System.out.println("\n=== 차단 목록 ===");
        List<String> blocked = currentUser.getBlockedList();
        if (blocked.isEmpty()) {
            System.out.println("차단된 사용자가 없습니다.");
        } else {
            for (String userId : blocked) {
                User user = users.get(userId);
                if (user != null) {
                    System.out.println("- " + user.getNickname() + " (" + userId + ")");
                }
            }
        }
    }
    
    private void searchFriend() {
        System.out.print("검색할 닉네임 또는 아이디: ");
        String keyword = scanner.nextLine().toLowerCase();
        
        System.out.println("\n=== 검색 결과 ===");
        boolean found = false;
        for (User user : users.values()) {
            if (!user.getUserId().equals(currentUser.getUserId()) &&
                (user.getNickname().toLowerCase().contains(keyword) || 
                 user.getUserId().toLowerCase().contains(keyword))) {
                System.out.println("- " + user.getNickname() + " (" + user.getUserId() + ")");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("검색 결과가 없습니다.");
        }
    }
    
    private void profileMenu() {
        System.out.println("\n=== 프로필 관리 ===");
        System.out.println("1. 프로필 보기");
        System.out.println("2. 닉네임 변경");
        System.out.println("3. 비밀번호 변경");
        System.out.println("4. 이메일 변경");
        System.out.println("5. 전화번호 변경");
        System.out.println("6. 메인 메뉴로");
        System.out.print("선택: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1: showProfile(); break;
            case 2: changeNickname(); break;
            case 3: changePassword(); break;
            case 4: changeEmail(); break;
            case 5: changePhone(); break;
            case 6: return;
            default:
                System.out.println("잘못된 선택입니다.");
        }
    }
    
    private void showProfile() {
        System.out.println("\n=== 내 프로필 ===");
        System.out.println("아이디: " + currentUser.getUserId());
        System.out.println("닉네임: " + currentUser.getNickname());
        System.out.println("이메일: " + currentUser.getEmail());
        System.out.println("전화번호: " + currentUser.getPhone());
        System.out.println("친구 수: " + currentUser.getFriendList().size());
    }
    
    private void changeNickname() {
        System.out.print("새 닉네임: ");
        String newNickname = scanner.nextLine();
        currentUser.setNickname(newNickname);
        System.out.println("닉네임이 변경되었습니다.");
    }
    
    private void changePassword() {
        System.out.print("현재 비밀번호: ");
        String currentPassword = scanner.nextLine();
        
        if (!currentUser.getPassword().equals(currentPassword)) {
            System.out.println("현재 비밀번호가 일치하지 않습니다.");
            return;
        }
        
        System.out.print("새 비밀번호: ");
        String newPassword = scanner.nextLine();
        currentUser.setPassword(newPassword);
        System.out.println("비밀번호가 변경되었습니다.");
    }
    
    private void changeEmail() {
        System.out.print("새 이메일: ");
        String newEmail = scanner.nextLine();
        currentUser.setEmail(newEmail);
        System.out.println("이메일이 변경되었습니다.");
    }
    
    private void changePhone() {
        System.out.print("새 전화번호: ");
        String newPhone = scanner.nextLine();
        currentUser.setPhone(newPhone);
        System.out.println("전화번호가 변경되었습니다.");
    }
    
    private void chatMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    채팅     ");
        System.out.println("-".repeat(50));
        System.out.println("1. 채팅방 목록 보기");
        System.out.println("2. 새 채팅 시작");
        System.out.println("3. 그룹 채팅방 생성");
        System.out.println("4. 랜덤 채팅");
        System.out.println("5. 채팅방 정렬 (최근순)");
        System.out.println("6. 채팅방 정렬 (안읽은순)");
        System.out.println("7. 채팅방 고정/해제");
        System.out.println("8. 채팅방 나가기");
        System.out.println("9. 메인 메뉴로");
        System.out.println("-".repeat(30));
        System.out.print("선택: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1: showChatRoomList(); break;
                case 2: startNewChat(); break;
                case 3: createGroupChat(); break;
                case 4: randomChat(); break;
                case 5: sortChatRoomsByTime(); break;
                case 6: sortChatRoomsByUnread(); break;
                case 7: toggleChatRoomPin(); break;
                case 8: leaveChatRoom(); break;
                case 9: return;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        } catch (Exception e) {
            System.out.println("❌ 숫자를 입력해주세요.");
            scanner.nextLine(); // 잘못된 입력 버퍼 비우기
        }
    }
    
    
    private void showChatRoomList() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    채팅방 목록    ");
        System.out.println("-".repeat(50));
        Map<String, ChatRoom> userChatRooms = currentUser.getChatRooms();
        
        if (userChatRooms.isEmpty()) {
            System.out.println("📭 채팅방이 없습니다.\n");
            return;
        }
        
        List<String> friendNames = new ArrayList<>();
        for (Map.Entry<String, ChatRoom> entry : userChatRooms.entrySet()) {
            ChatRoom room = entry.getValue();
            int unreadCount = room.getUnreadCount(currentUser.getUserId());
            String pinStatus = room.isPinned() ? "📌 " : "";
            String unreadStatus = unreadCount > 0 ? " (" + unreadCount + "개 안읽음)" : "";
            
            // 친구 이름 추출 (그룹채팅이 아닌 경우)
            String displayName = "";
            if (!room.isGroupChat()) {
                // 1:1 채팅방에서 상대방 찾기
                for (String participantId : room.getParticipants()) {
                    if (!participantId.equals(currentUser.getUserId())) {
                        User friend = users.get(participantId);
                        if (friend != null) {
                            displayName = friend.getNickname();
                            break;
                        }
                    }
                }
            } else {
                displayName = room.getRoomName();
            }
            
            friendNames.add(displayName);
            System.out.printf("[%d] %s%s%s\n", 
                friendNames.size(), pinStatus, displayName, unreadStatus);
            System.out.println("    💬 최근: " + room.getLastMessageTime());
        }
        
        System.out.println("=".repeat(50));
        System.out.print("입장할 채팅방 번호 (취소: 0): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 0) {
            return;
        }
        
        if (choice > 0 && choice <= friendNames.size()) {
            String selectedFriendName = friendNames.get(choice - 1);
            
            // 선택된 친구 이름으로 채팅방 찾기
            for (Map.Entry<String, ChatRoom> entry : userChatRooms.entrySet()) {
                ChatRoom room = entry.getValue();
                
                if (!room.isGroupChat()) {
                    // 1:1 채팅방에서 상대방 찾기
                    for (String participantId : room.getParticipants()) {
                        if (!participantId.equals(currentUser.getUserId())) {
                            User friend = users.get(participantId);
                            if (friend != null && friend.getNickname().equals(selectedFriendName)) {
                                enterChatRoomByRoom(room, selectedFriendName);
                                return;
                            }
                        }
                    }
                } else if (room.getRoomName().equals(selectedFriendName)) {
                    enterChatRoomByRoom(room, selectedFriendName);
                    return;
                }
            }
        } else {
            System.out.println("❌ 잘못된 번호입니다.");
        }
    }
    
    private void enterChatRoomByRoom(ChatRoom room, String displayName) {
        // 메시지를 읽음 처리
        for (Message msg : room.getMessages()) {
            if (!msg.getSenderId().equals(currentUser.getUserId())) {
                msg.setRead(true);
            }
        }
        
        while (true) {
            System.out.println("\n" + "═".repeat(60));
            System.out.println("💬 채팅방: " + displayName);
            System.out.println("═".repeat(60));
            showMessages(room);
            
            System.out.println("─".repeat(60));
            System.out.println("1. 메시지 전송    2. 메시지 검색    3. 메시지 고정/해제");
            System.out.println("4. 메시지 북마크  5. 메시지 삭제    6. 메시지 답장");
            System.out.println("7. 채팅방 나가기");
            System.out.println("─".repeat(60));
            System.out.print("선택: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1: sendMessage(room); break;
                case 2: searchMessages(room); break;
                case 3: toggleMessagePin(room); break;
                case 4: bookmarkMessage(room); break;
                case 5: deleteMessage(room); break;
                case 6: replyToMessage(room); break;
                case 7: return;
                default:
                    System.out.println("❌ 잘못된 선택입니다.");
            }
        }
    }
    
    private void startNewChat() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    새 채팅 시작     ");
        System.out.println("-".repeat(50));
        
        // 친구 목록 표시
        List<String> friends = currentUser.getFriendList();
        if (friends.isEmpty()) {
            System.out.println("❌ 친구가 없습니다. 먼저 친구를 추가해주세요.");
            return;
        }
        
        System.out.println("📋 친구 목록:");
        for (int i = 0; i < friends.size(); i++) {
            User friend = users.get(friends.get(i));
            if (friend != null) {
                System.out.printf("[%d] %s (%s)\n", i + 1, friend.getNickname(), friend.getUserId());
            }
        }
        
        System.out.println("-".repeat(30));
        System.out.print("채팅할 친구 번호 (취소: 0): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 0) {
            return;
        }
        
        if (choice < 1 || choice > friends.size()) {
            System.out.println("❌ 잘못된 번호입니다.");
            return;
        }
        
        String friendId = friends.get(choice - 1);
        User selectedFriend = users.get(friendId);
        
        if (selectedFriend == null) {
            System.out.println("❌ 존재하지 않는 사용자입니다.");
            return;
        }
        
        if (currentUser.isBlocked(friendId)) {
            System.out.println("❌ 차단된 사용자입니다.");
            return;
        }
        
        // 기존 채팅방이 있는지 확인
        ChatRoom existingRoom = findExistingChatRoom(friendId);
        if (existingRoom != null) {
            System.out.println("✅ 기존 채팅방으로 입장합니다: " + selectedFriend.getNickname());
            enterChatRoomByRoom(existingRoom, selectedFriend.getNickname());
            return;
        }
        
        // 새 채팅방 생성
        String roomId = "room_" + roomIdCounter++;
        ChatRoom chatRoom = new ChatRoom(roomId, selectedFriend.getNickname(), false);
        chatRoom.addParticipant(currentUser.getUserId());
        chatRoom.addParticipant(friendId);
        
        chatRooms.put(roomId, chatRoom);
        currentUser.getChatRooms().put(selectedFriend.getNickname(), chatRoom);
        
        // 상대방의 채팅방 목록에도 추가
        selectedFriend.getChatRooms().put(currentUser.getNickname(), chatRoom);
        
        System.out.println("✅ 새 채팅방이 생성되었습니다: " + selectedFriend.getNickname());
        enterChatRoomByRoom(chatRoom, selectedFriend.getNickname());
    }
    
    private ChatRoom findExistingChatRoom(String friendId) {
        for (ChatRoom room : currentUser.getChatRooms().values()) {
            if (!room.isGroupChat() && room.getParticipants().contains(friendId)) {
                return room;
            }
        }
        return null;
    }
    
    private void createGroupChat() {
        System.out.print("그룹 채팅방 이름: ");
        String roomName = scanner.nextLine();
        
        String roomId = "group_" + roomIdCounter++;
        ChatRoom chatRoom = new ChatRoom(roomId, roomName, true);
        chatRoom.addParticipant(currentUser.getUserId());
        
        System.out.println("초대할 친구들의 아이디를 입력하세요 (완료: enter):");
        while (true) {
            System.out.print("친구 아이디: ");
            String friendId = scanner.nextLine();
            
            if (friendId.isEmpty()) break;
            
            if (users.containsKey(friendId) && currentUser.isFriend(friendId)) {
                chatRoom.addParticipant(friendId);
                users.get(friendId).getChatRooms().put(roomName, chatRoom);
                System.out.println(friendId + "가 초대되었습니다.");
            } else {
                System.out.println("존재하지 않거나 친구가 아닌 사용자입니다.");
            }
        }
        
        chatRooms.put(roomId, chatRoom);
        currentUser.getChatRooms().put(roomName, chatRoom);
        
        System.out.println("그룹 채팅방이 생성되었습니다!");
    }
    
    private void randomChat() {
        List<String> availableUsers = new ArrayList<>();
        for (String userId : users.keySet()) {
            if (!userId.equals(currentUser.getUserId()) && 
                !currentUser.isBlocked(userId)) {
                availableUsers.add(userId);
            }
        }
        
        if (availableUsers.isEmpty()) {
            System.out.println("랜덤 채팅할 사용자가 없습니다.");
            return;
        }
        
        Random random = new Random();
        String randomUserId = availableUsers.get(random.nextInt(availableUsers.size()));
        
        System.out.println("랜덤 매칭: " + users.get(randomUserId).getNickname());
        
        String roomId = "random_" + roomIdCounter++;
        String roomName = "랜덤채팅_" + users.get(randomUserId).getNickname();
        
        ChatRoom chatRoom = new ChatRoom(roomId, roomName, false);
        chatRoom.addParticipant(currentUser.getUserId());
        chatRoom.addParticipant(randomUserId);
        
        chatRooms.put(roomId, chatRoom);
        currentUser.getChatRooms().put(roomName, chatRoom);
        users.get(randomUserId).getChatRooms().put("랜덤채팅_" + currentUser.getNickname(), chatRoom);
        
        enterChatRoom(roomName);
    }
    
        
    private void enterChatRoom(String roomName) {
        ChatRoom room = currentUser.getChatRooms().get(roomName);
        if (room == null) {
            System.out.println("❌ 채팅방을 찾을 수 없습니다.");
            return;
        }
        
        // 메시지를 읽음 처리
        for (Message msg : room.getMessages()) {
            if (!msg.getSenderId().equals(currentUser.getUserId())) {
                msg.setRead(true);
            }
        }
        
        while (true) {
            System.out.println("\n" + "═".repeat(60));
            System.out.println("💬 채팅방: " + roomName);
            System.out.println("═".repeat(60));
            showMessages(room);
            
            System.out.println("─".repeat(60));
            System.out.println("1. 메시지 전송    2. 메시지 검색    3. 메시지 고정/해제");
            System.out.println("4. 메시지 북마크  5. 메시지 삭제    6. 메시지 답장");
            System.out.println("7. 채팅방 나가기");
            System.out.println("─".repeat(60));
            System.out.print("선택: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1: sendMessage(room); break;
                    case 2: searchMessages(room); break;
                    case 3: toggleMessagePin(room); break;
                    case 4: bookmarkMessage(room); break;
                    case 5: deleteMessage(room); break;
                    case 6: replyToMessage(room); break;
                    case 7: return;
                    default:
                        System.out.println("❌ 잘못된 선택입니다.");
                }
            } catch (Exception e) {
                System.out.println("❌ 숫자를 입력해주세요.");
                scanner.nextLine(); // 잘못된 입력 버퍼 비우기
            }
        }
    }
    
    private void showMessages(ChatRoom room) {
        List<Message> messages = room.getMessages();
        if (messages.isEmpty()) {
            System.out.println("\n📭 메시지가 없습니다.");
            return;
        }
        
        // 메시지를 고정된 것과 일반 메시지로 분리하여 정렬
        List<Message> pinnedMessages = new ArrayList<>();
        List<Message> normalMessages = new ArrayList<>();
        
        for (Message msg : messages) {
            if (!msg.isDeleted()) {
                if (msg.isPinned()) {
                    pinnedMessages.add(msg);
                } else {
                    normalMessages.add(msg);
                }
            }
        }
        
        System.out.println("\n" + "─".repeat(60));
        System.out.println("📨 메시지 목록");
        System.out.println("─".repeat(60));
        
        int index = 0;
        
        // 고정된 메시지 먼저 표시
        if (!pinnedMessages.isEmpty()) {
            System.out.println("📌 고정된 메시지:");
            for (Message msg : pinnedMessages) {
                displayMessage(msg, index, messages);
                index++;
            }
            System.out.println("─".repeat(40));
        }
        
        // 일반 메시지 표시
        for (Message msg : normalMessages) {
            displayMessage(msg, index, messages);
            index++;
        }
        System.out.println("─".repeat(60));
    }
    
    private void displayMessage(Message msg, int displayIndex, List<Message> allMessages) {
        String senderName = users.get(msg.getSenderId()).getNickname();
        String status = "";
        
        if (msg.isPinned()) status += "📌 ";
        if (msg.isBookmarked()) status += "🔖 ";
        
        // 답장 메시지인 경우 원본 메시지 표시
        String replyInfo = "";
        if (msg.getReplyToMessageId() != null) {
            Message originalMsg = findMessageById(allMessages, msg.getReplyToMessageId());
            if (originalMsg != null && !originalMsg.isDeleted()) {
                String originalSender = users.get(originalMsg.getSenderId()).getNickname();
                String originalContent = originalMsg.getContent();
                if (originalContent.length() > 20) {
                    originalContent = originalContent.substring(0, 20) + "...";
                }
                replyInfo = "↩️  " + originalSender + ": " + originalContent;
            }
            status += "\n     ↳ ";
        }
        
        System.out.printf("[%d] %s%s: %s%s\n", 
            displayIndex, replyInfo, status, senderName, msg.getContent());
        System.out.println("    ⏰ " + msg.getTimestamp());
        System.out.println();
    }
    
    private Message findMessageById(List<Message> messages, String messageId) {
        for (Message msg : messages) {
            if (msg.getMessageId().equals(messageId)) {
                return msg;
            }
        }
        return null;
    }
    
    private void sendMessage(ChatRoom room) {
        System.out.println("\n" + "─".repeat(30));
        System.out.print("💬 메시지 (종료: -1 입력)");
       
        while (true) {
            System.out.print("\n💬 메시지: ");
            String content = scanner.nextLine();
            
            if (content.equals("-1")) {
                System.out.println("메시지 전송을 종료합니다.");
                return;
            }
            
            if (content.trim().isEmpty()) {
                System.out.println("❌ 메시지를 입력해주세요.");
                continue;
            }
            
            String messageId = "msg_" + messageIdCounter++;
            Message message = new Message(messageId, currentUser.getUserId(), content);
            room.addMessage(message);
            
            // 모든 참여자의 채팅방에 메시지 동기화
            syncMessageToAllParticipants(room, message);
            
            System.out.println("✅ 메시지가 전송되었습니다.");
            //System.out.println("─".repeat(30));
        }
    }
    
    private void syncMessageToAllParticipants(ChatRoom room, Message message) {
        for (String participantId : room.getParticipants()) {
            if (!participantId.equals(currentUser.getUserId())) {
                User participant = users.get(participantId);
                if (participant != null) {
                    // 참여자의 채팅방 목록에서 해당 채팅방 찾기
                    for (ChatRoom participantRoom : participant.getChatRooms().values()) {
                        if (participantRoom.getRoomId().equals(room.getRoomId())) {
                            // 이미 같은 객체를 참조하고 있으므로 자동으로 동기화됨
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private void searchMessages(ChatRoom room) {
    	 System.out.println("\n" + "─".repeat(50));
         System.out.println("🔍 메시지 검색 (종료: -1 입력)");
         System.out.println("─".repeat(50));
         
         while (true) {
             System.out.print("🔍 검색할 키워드: ");
             String keyword = scanner.nextLine();
             
             if (keyword.equals("-1")) {
                 System.out.println("메시지 검색을 종료합니다.");
                 return;
             }
             
             if (keyword.trim().isEmpty()) {
                 System.out.println("❌ 검색할 키워드를 입력해주세요.");
                 continue;
             }
             
             keyword = keyword.toLowerCase();2
             
             System.out.println("\n=== 검색 결과 ===");
             boolean found = false;
             
             for (int i = 0; i < room.getMessages().size(); i++) {
                 Message msg = room.getMessages().get(i);
                 if (!msg.isDeleted() && msg.getContent().toLowerCase().contains(keyword)) {
                     String senderName = users.get(msg.getSenderId()).getNickname();
                     System.out.println("[" + i + "] " + senderName + ": " + msg.getContent() + 
                                      " (" + msg.getTimestamp() + ")");
                     found = true;
                 }
             }
             
             if (!found) {
                 System.out.println("검색 결과가 없습니다.");
             }
             System.out.println("─".repeat(30));
         }
    }
    
    private void toggleMessagePin(ChatRoom room) {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("📌 메시지 고정/해제 (종료: -1 입력)");
        System.out.println("─".repeat(50));
        
        while (true) {
            if (room.getMessages().isEmpty()) {
                System.out.println("❌ 메시지가 없습니다.");
                return;
            }
            
            System.out.print("📌 고정/해제할 메시지 번호: ");
            String input = scanner.nextLine();
            
            if (input.equals("-1")) {
                System.out.println("메시지 고정/해제를 종료합니다.");
                return;
            }
            
            try {
                int index = Integer.parseInt(input);
                
                if (index < 0 || index >= room.getMessages().size()) {
                    System.out.println("❌ 잘못된 메시지 번호입니다.");
                    continue;
                }
                
                Message msg = room.getMessages().get(index);
                if (msg.isDeleted()) {
                    System.out.println("❌ 삭제된 메시지입니다.");
                    continue;
                }
                
                msg.setPinned(!msg.isPinned());
                System.out.println("✅ 메시지가 " + (msg.isPinned() ? "고정" : "고정 해제") + "되었습니다.");
                
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자를 입력해주세요.");
            }
        }
    }
    
    private void bookmarkMessage(ChatRoom room) {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("🔖 메시지 북마크 (종료: -1 입력)");
        System.out.println("─".repeat(50));
        
        while (true) {
            if (room.getMessages().isEmpty()) {
                System.out.println("❌ 메시지가 없습니다.");
                return;
            }
            
            System.out.print("🔖 북마크할 메시지 번호: ");
            String input = scanner.nextLine();
            
            if (input.equals("-1")) {
                System.out.println("메시지 북마크를 종료합니다.");
                return;
            }
            
            try {
                int index = Integer.parseInt(input);
                
                if (index < 0 || index >= room.getMessages().size()) {
                    System.out.println("❌ 잘못된 메시지 번호입니다.");
                    continue;
                }
                
                Message msg = room.getMessages().get(index);
                if (msg.isDeleted()) {
                    System.out.println("❌ 삭제된 메시지입니다.");
                    continue;
                }
                
                msg.setBookmarked(!msg.isBookmarked());
                System.out.println("✅ 메시지가 " + (msg.isBookmarked() ? "북마크에 추가" : "북마크에서 제거") + "되었습니다.");
                System.out.println("─".repeat(30));
                
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자를 입력해주세요.");
            }
        }
    }
    
    private void deleteMessage(ChatRoom room) {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("🗑️ 메시지 삭제 (종료: -1 입력)");
        System.out.println("─".repeat(50));
        
        while (true) {
            if (room.getMessages().isEmpty()) {
                System.out.println("❌ 메시지가 없습니다.");
                return;
            }
            
            System.out.print("🗑️ 삭제할 메시지 번호: ");
            String input = scanner.nextLine();
            
            if (input.equals("-1")) {
                System.out.println("메시지 삭제를 종료합니다.");
                return;
            }
            
            try {
                int index = Integer.parseInt(input);
                
                if (index < 0 || index >= room.getMessages().size()) {
                    System.out.println("❌ 잘못된 메시지 번호입니다.");
                    continue;
                }
                
                Message msg = room.getMessages().get(index);
                if (msg.isDeleted()) {
                    System.out.println("❌ 이미 삭제된 메시지입니다.");
                    continue;
                }
                
                if (!msg.getSenderId().equals(currentUser.getUserId())) {
                    System.out.println("❌ 자신의 메시지만 삭제할 수 있습니다.");
                    continue;
                }
                
                System.out.println("1. 나에게만 삭제");
                System.out.println("2. 모두에게 삭제");
                System.out.print("선택: ");
                String choiceInput = scanner.nextLine();
                
                if (choiceInput.equals("-1")) {
                    continue; // 삭제 취소하고 다시 메시지 번호 입력으로
                }
                
                try {
                    int choice = Integer.parseInt(choiceInput);
                    
                    if (choice == 1) {
                        // 실제 구현에서는 사용자별로 삭제 상태를 관리해야 함
                        System.out.println("✅ 나에게만 메시지가 삭제되었습니다. (현재는 모두에게 삭제됨)");
                        msg.setDeleted(true);
                    } else if (choice == 2) {
                        msg.setDeleted(true);
                        System.out.println("✅ 모두에게 메시지가 삭제되었습니다.");
                    } else {
                        System.out.println("❌ 잘못된 선택입니다.");
                        continue;
                    }
                    System.out.println("─".repeat(30));
                    
                } catch (NumberFormatException e) {
                    System.out.println("❌ 숫자를 입력해주세요.");
                }
                
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자를 입력해주세요.");
            }
        }
    }
    
    private void replyToMessage(ChatRoom room) {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("↩️ 메시지 답장 (종료: -1 입력)");
        System.out.println("─".repeat(50));
        
        while (true) {
            if (room.getMessages().isEmpty()) {
                System.out.println("❌ 메시지가 없습니다.");
                return;
            }
            
            System.out.print("↩️ 답장할 메시지 번호: ");
            String input = scanner.nextLine();
            
            if (input.equals("-1")) {
                System.out.println("메시지 답장을 종료합니다.");
                return;
            }
            
            try {
                int index = Integer.parseInt(input);
                
                // 실제 메시지 리스트에서의 인덱스 찾기
                List<Message> visibleMessages = new ArrayList<>();
                for (Message msg : room.getMessages()) {
                    if (!msg.isDeleted()) {
                        visibleMessages.add(msg);
                    }
                }
                
                if (index < 0 || index >= visibleMessages.size()) {
                    System.out.println("❌ 잘못된 메시지 번호입니다.");
                    continue;
                }
                
                Message originalMsg = visibleMessages.get(index);
                String originalSender = users.get(originalMsg.getSenderId()).getNickname();
                
                System.out.println("📩 답장할 메시지: " + originalSender + ": " + originalMsg.getContent());
                System.out.print("\n💬 답장 내용: ");
                String replyContent = scanner.nextLine();
                
                if (replyContent.equals("-1")) {
                    continue; // 답장 취소하고 다시 메시지 번호 입력으로
                }
                
                if (replyContent.trim().isEmpty()) {
                    System.out.println("❌ 답장 내용을 입력해주세요.");
                    continue;
                }
                
                String messageId = "msg_" + messageIdCounter++;
                Message replyMsg = new Message(messageId, currentUser.getUserId(), replyContent);
                replyMsg.setReplyToMessageId(originalMsg.getMessageId());
                room.addMessage(replyMsg);
                
                // 모든 참여자의 채팅방에 답장 메시지 동기화
                syncMessageToAllParticipants(room, replyMsg);
                
                System.out.println("✅ 답장이 전송되었습니다.");
                
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자를 입력해주세요.");
            }
        }
    }
    
    private void sortChatRoomsByTime() {
        Map<String, ChatRoom> userChatRooms = currentUser.getChatRooms();
        
        userChatRooms.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().getLastMessageTime().compareTo(e1.getValue().getLastMessageTime()))
            .forEach(entry -> {
                ChatRoom room = entry.getValue();
                int unreadCount = room.getUnreadCount(currentUser.getUserId());
                String pinStatus = room.isPinned() ? "[고정] " : "";
                String unreadStatus = unreadCount > 0 ? " (" + unreadCount + ")" : "";
                
                System.out.println(pinStatus + room.getRoomName() + unreadStatus + 
                                 " - 최근: " + room.getLastMessageTime());
            });
    }
    
    private void sortChatRoomsByUnread() {
        Map<String, ChatRoom> userChatRooms = currentUser.getChatRooms();
        
        userChatRooms.entrySet().stream()
            .sorted((e1, e2) -> Integer.compare(
                e2.getValue().getUnreadCount(currentUser.getUserId()),
                e1.getValue().getUnreadCount(currentUser.getUserId())))
            .forEach(entry -> {
                ChatRoom room = entry.getValue();
                int unreadCount = room.getUnreadCount(currentUser.getUserId());
                String pinStatus = room.isPinned() ? "[고정] " : "";
                String unreadStatus = unreadCount > 0 ? " (" + unreadCount + ")" : "";
                
                System.out.println(pinStatus + room.getRoomName() + unreadStatus + 
                                 " - 안읽은 메시지: " + unreadCount);
            });
    }
    
    private void toggleChatRoomPin() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("=== 채팅방 고정/해제 ===");
        System.out.println("=".repeat(50));
        
        Map<String, ChatRoom> userChatRooms = currentUser.getChatRooms();
        if (userChatRooms.isEmpty()) {
            System.out.println("❌ 채팅방이 없습니다.");
            return;
        }
        
        List<String> friendNames = new ArrayList<>();
        for (Map.Entry<String, ChatRoom> entry : userChatRooms.entrySet()) {
            ChatRoom room = entry.getValue();
            String pinStatus = room.isPinned() ? "📌 " : "";
            
            String displayName = "";
            if (!room.isGroupChat()) {
                for (String participantId : room.getParticipants()) {
                    if (!participantId.equals(currentUser.getUserId())) {
                        User friend = users.get(participantId);
                        if (friend != null) {
                            displayName = friend.getNickname();
                            break;
                        }
                    }
                }
            } else {
                displayName = room.getRoomName();
            }
            
            friendNames.add(displayName);
            System.out.printf("[%d] %s%s\n", friendNames.size(), pinStatus, displayName);
        }
        
        System.out.println("=".repeat(50));
        System.out.print("고정/해제할 채팅방 번호: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice < 1 || choice > friendNames.size()) {
            System.out.println("❌ 잘못된 번호입니다.");
            return;
        }
        
        String selectedName = friendNames.get(choice - 1);
        
        // 선택된 이름으로 채팅방 찾기
        for (ChatRoom room : userChatRooms.values()) {
            String roomDisplayName = "";
            if (!room.isGroupChat()) {
                for (String participantId : room.getParticipants()) {
                    if (!participantId.equals(currentUser.getUserId())) {
                        User friend = users.get(participantId);
                        if (friend != null) {
                            roomDisplayName = friend.getNickname();
                            break;
                        }
                    }
                }
            } else {
                roomDisplayName = room.getRoomName();
            }
            
            if (roomDisplayName.equals(selectedName)) {
                room.setPinned(!room.isPinned());
                System.out.println("✅ 채팅방이 " + (room.isPinned() ? "고정" : "고정 해제") + "되었습니다.");
                return;
            }
        }
    }
    
    private void leaveChatRoom() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    채팅방 나가기    ");
        System.out.println("=".repeat(50));
        
        Map<String, ChatRoom> userChatRooms = currentUser.getChatRooms();
        if (userChatRooms.isEmpty()) {
            System.out.println("❌ 채팅방이 없습니다.");
            return;
        }
        
        List<String> friendNames = new ArrayList<>();
        List<ChatRoom> roomList = new ArrayList<>();
        
        for (Map.Entry<String, ChatRoom> entry : userChatRooms.entrySet()) {
            ChatRoom room = entry.getValue();
            
            String displayName = "";
            if (!room.isGroupChat()) {
                for (String participantId : room.getParticipants()) {
                    if (!participantId.equals(currentUser.getUserId())) {
                        User friend = users.get(participantId);
                        if (friend != null) {
                            displayName = friend.getNickname();
                            break;
                        }
                    }
                }
            } else {
                displayName = room.getRoomName();
            }
            
            friendNames.add(displayName);
            roomList.add(room);
            System.out.printf("[%d] %s\n", friendNames.size(), displayName);
        }
        
        System.out.println("=".repeat(50));
        System.out.print("나갈 채팅방 번호: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice < 1 || choice > friendNames.size()) {
            System.out.println("❌ 잘못된 번호입니다.");
            return;
        }
        
        ChatRoom selectedRoom = roomList.get(choice - 1);
        String selectedName = friendNames.get(choice - 1);
        
        selectedRoom.removeParticipant(currentUser.getUserId());
        
        // 현재 사용자의 채팅방 목록에서 제거
        currentUser.getChatRooms().entrySet().removeIf(entry -> 
            entry.getValue().getRoomId().equals(selectedRoom.getRoomId()));
        
        System.out.println("✅ " + selectedName + " 채팅방에서 나갔습니다.");
        
        // 그룹 채팅방에서 나가기 메시지 추가
        if (selectedRoom.isGroupChat()) {
            String messageId = "msg_" + messageIdCounter++;
            Message leaveMsg = new Message(messageId, "SYSTEM", 
                currentUser.getNickname() + "님이 채팅방을 나갔습니다.");
            selectedRoom.addMessage(leaveMsg);
        }
    }
    
    public static void main(String[] args) {
        ChatApplication app = new ChatApplication();
        app.start();
    }
}
