package me.dack.wenda.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.dack.wenda.model.Errcode;
import me.dack.wenda.model.HostHolder;
import me.dack.wenda.model.Message;
import me.dack.wenda.model.Result;
import me.dack.wenda.model.User;
import me.dack.wenda.service.MessageService;

@Controller
@RequestMapping("/message")
public class MessageController {

	private static Logger logger = LoggerFactory.getLogger(MessageController.class);
	@Autowired
	private MessageService messageService;
	@Autowired
	private HostHolder hostHolder;
	
	@RequestMapping("/addMessage")
	@ResponseBody
	public Result addMessage(int toId,String content){
		Message message = new Message();
		User user = hostHolder.getUser();
		message.setFromId(user.getId()); 
		
		message.setToId(toId);
		message.setContent(content);
		message.setCreateTime(new Date());
		message.setHasRead(0);
		try{
			if(messageService.addMessage(message) > 0){
				return new Result(Errcode.Null,"添加成功");
			}
		}catch (Exception e) {
			logger.error("添加消息失败，"+e.getMessage());
		}
		return new Result(Errcode.Error,"添加失败");
	}
	
	@RequestMapping("/getConversationDetail")
	@ResponseBody
	public Result getConversationDetail(String conversationId,int offset,int limit){
		try{
			List<Message> conversationDetail = messageService.getConversationDetail(conversationId, offset, limit);
			Result result = new Result(Errcode.Null,"查找成功");
			result.setRes(conversationDetail);
			return result;
		}catch (Exception e) {
			logger.error("查找会话消息失败，"+e.getMessage());
		}
		return new Result(Errcode.Error,"查找失败");
	}
	
	@RequestMapping("/getConversationList")
	@ResponseBody
	public Result getConversationList(int userId,int offset,int limit){
		try{
			List<Message> conversationList = messageService.getConversationList(userId, offset, limit);
			Result result = new Result(Errcode.Null,"查找成功");
			result.setRes(conversationList);
			return result;
		}catch (Exception e) {
			logger.error("查找会话消息列表失败，"+e.getMessage());
		}
		return new Result(Errcode.Error,"查找失败");
	}
	
	@RequestMapping("/getConversationUnReadCount")
	@ResponseBody
	public Result getConversationUnReadCount(int userId,String conversationId){
		try{
			int conversationUnReadCount = messageService.getConversationUnReadCount(userId, conversationId);
			Result result = new Result(Errcode.Null,"查找成功");
			result.setRes(conversationUnReadCount);
			return result;
		}catch (Exception e) {
			logger.error("查找未读消息数量失败，"+e.getMessage());
		}
		return new Result(Errcode.Error,"查找失败");
	}
	
	@RequestMapping("/updateConversationHasRead")
	@ResponseBody
	public Result updateConversationHasRead(int userId,String conversationId){
		try{
			if(messageService.updateConversationHasRead(userId, conversationId) > 0){
				Result result = new Result(Errcode.Null,"修改成功");
				return result;
			}
		}catch (Exception e) {
			logger.error("修改消息已读失败，"+e.getMessage());
		}
		return new Result(Errcode.Error,"修改失败");
	}
}
