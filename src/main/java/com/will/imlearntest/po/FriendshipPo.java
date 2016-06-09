package com.will.imlearntest.po;

/**
 * Created by willl on 5/26/16.
 */
public class FriendshipPo {
    private int userId;
    private int friendId;
    private String groupName;
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }
    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }
    public int getFriendId() {
        return friendId;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getGroupName() {
        return groupName;
    }
}
