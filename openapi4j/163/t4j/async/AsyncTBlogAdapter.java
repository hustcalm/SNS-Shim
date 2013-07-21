/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package t4j.async;

import java.util.List;

import t4j.TBlogException;
import t4j.data.Comment;
import t4j.data.DirectMessage;
import t4j.data.PagableResponseList;
import t4j.data.Status;
import t4j.data.User;
import t4j.data.Venue;

/**
 * 
 * @author gcwang
 * 
 * 如果要处理相应的事件，覆盖以下事件的处理
 *
 */
public class AsyncTBlogAdapter implements TBlogListener {

	@Override
	public void onException(TBlogException te, int method) {
	}

	@Override
	public void gotHomeTimeline(List<Status> homeTimeline) {
	}

	@Override
	public void gotPublicTimeline(List<Status> publicTimeline) {
	}

	@Override
	public void gotMentions(List<Status> mentions) {
	}

	@Override
	public void gotUserTimeline(List<Status> userTimeline) {
	}

	@Override
	public void gotRetweetsOfMe(List<Status> retweetsOfMe) {
	}

	@Override
	public void updatedStatus(Status status) {
	}

	@Override
	public void retweeted(Status retweet) {
	}

	@Override
	public void shownStatus(Status showStatus) {
	}

	@Override
	public void destoryed(Status destroy) {
	}

	@Override
	public void shownUser(User showUser) {
	}

	@Override
	public void gotFriends(PagableResponseList<User> friends) {
	}

	@Override
	public void gotFollowers(PagableResponseList<User> followers) {
	}

	@Override
	public void createdFriendship(User createFriendship) {
	}

	@Override
	public void destroyedFriendship(User destroyFriendship) {
	}

	@Override
	public void gotDirectMessage(List<DirectMessage> directMessages) {
	}

	@Override
	public void sentDirectMessage(DirectMessage sendDirectMessage) {
	}

	@Override
	public void destroyedDirectMessage(DirectMessage destroyDirectMessage) {
	}

	@Override
	public void gotSentDirectMessages(List<DirectMessage> sentDirectMessages) {
	}

	@Override
	public void gotFavorites(List<Status> favorites) {
	}

	@Override
	public void createdFavorites(Status createFavorite) {
	}

	@Override
	public void destroyedFavorite(Status destroyFavorite) {
	}

	@Override
	public void verifiedCredentials(User verifyCredentials) {
	}

	@Override
	public void createdBlock(User createBlock) {
	}

	@Override
	public void destroyedBlock(User destroyBlock) {
	}

	@Override
	public void existsBlock(User existsBlock) {
	}

	@Override
	public void gotBlockUsers(List<User> blockUsers) {
	}

	@Override
	public void gotBlockUserIds(long[] blockUserIds) {
	}

	@Override
	public void updatedImage(String imageURL) {
	}

	@Override
	public void searchedStatus(List<Status> status) {
	}

	@Override
	public void searchedUsers(List<User> searchUser) {
	}

	@Override
	public void gotVenues(List<Venue> venues) {
	}

	@Override
	public void gotLocationTimeline(List<Status> locationTimeline) {
	}

	@Override
	public void gotComments(List<Comment> comments) {
	}
}
