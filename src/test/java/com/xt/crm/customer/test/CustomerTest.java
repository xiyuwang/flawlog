package com.i2s.flawlog.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.xt.common.constant.KafkaConstant;
import com.xt.common.constant.RedisConstant;
import com.xt.common.entity.ResponseEntity;
import com.xt.common.entity.User;
import com.xt.common.util.ChkUtil;
import com.xt.common.util.JsonUtil;
import com.i2s.flawlog.CustomerApplication;
import com.i2s.flawlog.config.DependencyConfig;
import com.i2s.flawlog.config.RedisClient;
import com.i2s.flawlog.converter.AssetConverter;
import com.i2s.flawlog.converter.CardConverter;
import com.i2s.flawlog.converter.CustomerConverter;
import com.i2s.flawlog.counter.CustomerCount;
import com.i2s.flawlog.domain.vo.CardUtilVO;
import com.i2s.flawlog.domain.vo.ChangeCardVO;
import com.i2s.flawlog.domain.vo.ConsumeRecordVO;
import com.i2s.flawlog.domain.vo.ConsumeUtilVO;
import com.i2s.flawlog.domain.vo.CusBirthdayVO;
import com.i2s.flawlog.util.MessageSender;
import com.i2s.flawlog.util.RoleUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerApplication.class)
public class CustomerTest{
	@Autowired
	CustomerService customerService;
	
	@Autowired
	RedisClient redisClient;
	
	@Autowired
	DictService dictService;
	
	@Autowired
	MessageSender messageSender;
	
	@Autowired
	DependencyConfig dep;
	
	@Autowired
	RestTemplate restTemplate;

	@SuppressWarnings("unchecked")
	@Test
	public void getOrgIdsTest(){
		String path = "http://192.168.40.230:8081/permission/getDepartMentOrgId?id=123456";//0459026525836
		ResponseEntity data = restTemplate.postForObject(path, "", ResponseEntity.class);
		if(ChkUtil.isEmpty(data.getData())){
			System.out.println("空的");
		}else{
			Map<String,List<String>> map = (Map<String,List<String>>)data.getData();
			List<String> ids = (List<String>)map.get("ids");
			System.err.println(JsonUtil.beanToJson(ids));
		}
	}
	
	//会员跟进
	@Test
	public void insertCusFollowTest(){
		FollowBO fo = new FollowBO();
		fo.setCardId("20170801");
		fo.setCustomerId("892559434327261184");
		fo.setOrgId("1");
		fo.setFollowTime("2017-08-21 10:53:33");
		fo.setContactWay(1);
		fo.setValuableContent("有价值的跟进内容");
		fo.setFollowContent("跟进内容");
		fo.setCreatedBy(1L);
		fo.setOperatorId("123456789");
		fo.setOperatorName("更近人员a");
		int i = -1;
		try{
			i = customerService.insertCusFollow(fo);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(i);
	}
	
	@Test
	public void queryFollowTest(){
		FollowUtilBO f = new FollowUtilBO();
		f.setCardId("20170801");
		f.setCustomerId("892559434327261184");
		f.setPage(1);
		f.setPageSize(2);
		ListVO list = customerService.queryFollowRecord(f);
		System.err.println(JsonUtil.beanToJson(list));
	}
	
	//kafka测试
	@Test
	public void insertRankTest(){
		RankBO rankBO = new RankBO();
		rankBO.setRank(1);
		rankBO.setRankName("特价会员");
		int info = -1;
		try{
			info = customerService.insertRank(rankBO);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(info);
//		messageSender.sendMessage(CustomerConstant.CREATE, rankBO, KafkaConstant.KFK_TOPIC_CUSTOMER_RANK);
	}
	
	@Test
	public void queryCusByOrg(){
		String userId = "094443081736904045";
		OrgBO orgBO = new OrgBO();
		List<Integer> orgs = RoleUtil.getDepartMentOrgId(dep, restTemplate, userId);
		
		if(ChkUtil.isEmpty(orgs)){
			List<String> userIds = RoleUtil.getDepartMentUser(dep, restTemplate, userId);
			orgBO.setUserIds(userIds);
		}else if(orgs.size() <= 0){
			List<String> userIds = RoleUtil.getDepartMentUser(dep, restTemplate, userId);
			orgBO.setUserIds(userIds);
		}else{
			orgBO.setOrgs(orgs);
		}
		List<CusByOrgVO> data = null;
		try{
			data = customerService.queryCusByOrg(orgBO);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(JsonUtil.beanToJson(data));
	}
	
	//会员办卡
	@Test
	public void insertCustomerRefCardTest(){
		CardRefCusVO cardRefCusVO = new CardRefCusVO();
		cardRefCusVO.setMobile("182716747744");
		cardRefCusVO.setGender(1);
		cardRefCusVO.setCustomerName("recharge");
		cardRefCusVO.setIdentificationNo("510921199409224369");
		cardRefCusVO.setProvince(000);
		cardRefCusVO.setCity(111);
		cardRefCusVO.setDistrict(222);
		cardRefCusVO.setAddress("共富新村5");
		cardRefCusVO.setBirthday("1988-04-07");
		cardRefCusVO.setEmail("316541@qq.com");
		cardRefCusVO.setTel("18271674545");
		cardRefCusVO.setNote("添加第七个会员");
		
		
		cardRefCusVO.setCardId("2017101216987458");
		cardRefCusVO.setChannel(1);
		cardRefCusVO.setType(1);
		cardRefCusVO.setState(1);
		cardRefCusVO.setValidBegin("2017-08-14");
		cardRefCusVO.setValidEnd("2027-08-14");
		cardRefCusVO.setIssuefee(6.00);
		cardRefCusVO.setOrgId("10886877");
		cardRefCusVO.setOrgName("前端组");
		cardRefCusVO.setServSpecialistId("04512446358848");
		cardRefCusVO.setServSpecialistName("孔庆芬");
		cardRefCusVO.setOperatorId("03564268423981");
		cardRefCusVO.setOperatorName("王贤");
		
		CustomerBO customerBO = CustomerConverter.buildCustomerRefBO(cardRefCusVO);
		CardBO cardBO = CardConverter.buildCardBO(cardRefCusVO);
		customerBO.setCityCode("999");
		customerBO.setCreatedBy(999L);
		cardBO.setCreatedBy(999L);
		
		Customer customer = CustomerConverter.getCustomer(customerBO);
		System.err.println(JsonUtil.beanToJson(customer));
		
//		int info = -1; 
//		try {
//			info = this.customerService.insertCustomerRefCard(customerBO, cardBO);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.err.println(info);
		Customer customer1 = CustomerConverter.getCustomer(customerBO);
		System.out.println(JsonUtil.beanToJson(customer1));
//		messageSender.sendMessage(KafkaConstant.KFK_OPERATION_CREATE, customer1, "customer_cust");
		CustomerCount.increaseAsyncreqsent();
		
		//kafka 新增会员卡通知 
		Card card = CardConverter.getCard(cardBO);
		messageSender.sendMessage(KafkaConstant.KFK_OPERATION_CREATE, card, "customer_card");
		CustomerCount.increaseAsyncreqsent();
		
	}
	
	//会员充值
	@Test
	public void rechargeAssetTest(){
		AssetVO as = new AssetVO();
		as.setLinkId("20170802022");
		as.setNumber(6.00);
		as.setPayway(1);
		as.setOperatorId("222");
		as.setOperatorName("666操作人员");
		as.setNote("续卡");
		
		AssetBO ass = AssetConverter.buildAssetBO(as);
		int info = -1;
		
		try{
			info = this.customerService.rechargeAsset(ass);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(info);
	}

	//查询会员卡列表
	@Test
	public void queryCardListTest(){
		CardListUtilVO dto = new CardListUtilVO();
		dto.setCardId("");
//		dto.setCardState(1);
		dto.setCusName("");
		dto.setMobile("");
		dto.setPage(1);
		dto.setPageSize(10);
		
		CardListUtilBO db = CardConverter.buildCardListUtilBO(dto);
		
		String userId = "04512558574944";
//		String userId = "0459026525836";
//		String userId = "666";
		db.setUserId(userId);
		List<Integer> orgs = RoleUtil.getDepartMentOrgId(dep, restTemplate, userId);
		if(ChkUtil.isEmpty(orgs)){
			List<String> userIds = RoleUtil.getDepartMentUser(dep, restTemplate, userId);
			db.setUserIds(userIds);
		}else if(orgs.size() <= 0){
			List<String> userIds = RoleUtil.getDepartMentUser(dep, restTemplate, userId);
			db.setUserIds(userIds);
		}else{
			db.setOrgs(orgs);
		}
		
		ListVO cardList = null;
		try{
			cardList = customerService.queryCardList(db);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(JsonUtil.beanToJson(cardList));
	}
	
	//会员卡挂失
	@Test
	public void changeCardStateTest(){
		CardStateVO ca = new CardStateVO();
		ca.setCardId("20170826");
		ca.setCusId("900650337277313024");
		CardStateBO cb = CardConverter.buildCardStateBO(ca);
		cb.setModifiedBy(666L);
		int info = -1;
		try{
			info = customerService.changeCardState(cb);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(info);
	}

	//会员换卡
	@Test
	public void exchangeCardTest(){
		ChangeCardVO ca = new ChangeCardVO();
		ca.setCardId("201240123");
		ca.setNewCardId("201240124");
		ChangeCardBO cb = CardConverter.buildChangeCardBO(ca);
		cb.setModifiedBy(666L);
		
		int info = -1;
		try{
			info = customerService.changeCard(cb);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.err.println(info);
	}
	
	//会员卡注销
	@Test
	public void unbindCardTest(){
		CardStateVO ca = new CardStateVO();
		ca.setCardId("20170819");
		ca.setCusId("900652357719687168");
		CardStateBO cb = CardConverter.buildCardStateBO(ca);
		cb.setModifiedBy(666L);
		int info = -1;
		try{
			info = customerService.unbindCard(cb);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.err.println(info);
	}
	

	//查询会员充值列表
	@Test
	public void queryRechargeListTest(){
		RechargeUtilVO as = new RechargeUtilVO();
//		as.setCustomerName("妞妞");
//		as.setMobile("15800568056");
//		as.setCardId("20170802006");
//		as.setBeginTime("2017-07-31 10:00:00");
//		as.setEndTime("2017-08-31 10:00:00");

		as.setPage(1);
		as.setPageSize(4);
		
		RechargeUtilBO  ass = AssetConverter.buildRechargeUtilBO(as);
		String userId = "0459026525836";
		List<Integer> orgs = RoleUtil.getDepartMentOrgId(dep, restTemplate, userId);
		ass.setOrgs(orgs);
		ass.setUserId(userId);
		ListVO cardList = null;
		try{
			cardList = customerService.queryRechargeList(ass);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(JsonUtil.beanToJson(cardList));
	}

	//查询会员卡基本属性
	@Test
	public void queryCardInfoTest(){
		CardUtilVO ca = new CardUtilVO();
		ca.setCardId("20170801");
		CardUtilBO cab = CardConverter.buildCardUtilBO(ca);
//		CardVO cardVO = customerService.queryCardInfo(cab);
//		System.err.println(JsonUtil.beanToJson(cardVO));
	}

	//查询会员基本属性
	@Test
	public void queryCustomerInfoTest(){
		CardUtilVO ca = new CardUtilVO();
		ca.setCardId("97898797897");
		CardUtilBO cab = CardConverter.buildCardUtilBO(ca);
		cab.setUserId("04512446358848");
		CustomerVO customerVO = customerService.queryCustomerInfo(cab);
//		
		System.err.println(JsonUtil.beanToJson(customerVO));
		
	}

	@Test
	public void exportConsume(){
		ExportBO ex = new ExportBO();
		List<Long> ids = new ArrayList<Long>();
		//321,320,319,318,317,316,314,304,303,299
		ids.add(321L);
		ids.add(320L);
		ids.add(319L);
		ex.setIds(ids);
		List<ConsumeRecordVO> assetList = customerService.exportConsumeList(ex);
		System.err.println(JsonUtil.beanToJson(assetList));
		
	}
	
	@Test
	public void exportRecharge(){
		ExportBO ex = new ExportBO();
		List<Long> ids = new ArrayList<Long>();
		//323,322,315,313,312,311,310,309,308,300
		ids.add(323L);
		ids.add(322L);
		ids.add(315L);
		ex.setIds(ids);
		List<RechargeRecordVO> assetList = customerService.exportRechargeList(ex);
		System.err.println(JsonUtil.beanToJson(assetList));
		
	}
	
	//是否满足购买限制
	@Test
	public void canBuyTest(){
		BuyVO buyVO = new BuyVO();
		buyVO.setAssetType(2);
		buyVO.setCardId("20171023001");
		
		BuyBO buyBO = AssetConverter.buildBuyBO(buyVO);
		Boolean flag = false;
		try{
			flag = customerService.canBuy(buyBO);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.err.println(flag);
	}
	
	//扣除资产
	@Test
	public void deductAssetTest(){
		AssetBO assetBO = new AssetBO();
		assetBO.setLinkId("33333322");
		assetBO.setAssetType(3);
		assetBO.setOperatorId("03564268423981");
		assetBO.setOperatorName("盛星星");
		assetBO.setNote("第一次特价消费");
		
		int info = -1;
		
		try{
			info = customerService.deductAsset(assetBO);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.err.println(info);
	}
	
	//查询消费列表
	@Test
	public void queryResumeListTest(){
		ConsumeUtilVO as = new ConsumeUtilVO();
//		as.setCustomerName("summer");
//		as.setMobile("15800568054");
//		as.setCardId("20170801");
//		as.setBeginTime("2017-08-10 10:00:00");
//		as.setEndTime("2017-08-31 10:00:00");

		as.setPage(1);
		as.setPageSize(10);
		
		ConsumeUtilBO ass = AssetConverter.buildConsumeUtilBO(as);
		String userId = "0459026525836";
		List<Integer> orgs = RoleUtil.getDepartMentOrgId(dep, restTemplate, userId);
		ass.setOrgs(orgs);
		ass.setUserId(userId);
		
		ListVO cardList = null;
		try{
			cardList = customerService.queryConsumeList(ass);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(JsonUtil.beanToJson(cardList));
		
	}
	
	//查询个人充值记录
	@Test
	public void querySingleRechargeTest(){
		SingleVO as = new SingleVO();
		as.setCardId("20170801");
		as.setPage(1);
		as.setPageSize(4);
		
		SingleBO ass = AssetConverter.buildSingleBO(as);
		ListVO consumeList = null;
		try{
			consumeList = customerService.querySingleRecharge(ass);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(JsonUtil.beanToJson(consumeList));
	}
	
	//查询个人消费记录
	@Test
	public void querySingleConsumeTest(){
		SingleVO as = new SingleVO();
		as.setCardId("20170801");
		as.setPage(1);
		as.setPageSize(4);
		
		SingleBO ass = AssetConverter.buildSingleBO(as);
		ListVO consumeList = null;
		try{
			consumeList = customerService.querySingleConsume(ass);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(JsonUtil.beanToJson(consumeList));
	}
	
	//查询会员生日列表
	@Test
	public void queryCustomerBirthdayTest(){
		CusBirthdayVO cu = new CusBirthdayVO();
		cu.setPage(1);
		cu.setPageSize(3);
		CusBirthdayBO cuu = CustomerConverter.buildCusBirthdayBO(cu);
//		String childComIds = "11085644,11085645,11085646,11085647,30515630,11086563";//调用获取客服人员Id方法
//		String[] childs = childComIds.split(",");
//		List<String> orgs = Arrays.asList(childs);
//		cuu.setOrgs(orgs);
		cuu.setUserId("1031314567864235ss");
		String userIds = "03564268423981,1031314567864235,08544450046941";//调用获取客服人员Id方法
		String[] idss = userIds.split(",");
		cuu.setUserIds(Arrays.asList(idss));
		ListVO consumeList = null;
		try{
			consumeList = customerService.queryCustomerBirthday(cuu);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(JsonUtil.beanToJson(consumeList));
		
	}

	//修改会员信息
	@Test
	public void updateCustomerInfoTest(){
		CustomerParaVO cu = new CustomerParaVO();
		cu.setCustomerId("923363390921900032");
		cu.setMobile("13986719986");
		cu.setCustomerName("庞广伟后");
		cu.setBirthday("1989-08-03");
		cu.setProvince(237);
		cu.setCity(417);
		cu.setDistrict(420);
		cu.setAddress("新地址");
		cu.setEmail("newEmail@126.com");
		cu.setTel("165464");
		cu.setNote("12");
		
		CustomerBO customerBO = CustomerConverter.buildCustomerBO(cu);
		customerBO.setModifiedBy(666L);
		int info = -1;
		
		try{
			info = customerService.updateCustomerInfo(customerBO);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.err.println(info);
	}
	
	//设置会员属性
	@Test
	public void updateCusAttribute(){
		CusAttributeBO at = new CusAttributeBO();
//		at.setCustomerId("892581752097210368");
		at.setOfficialRank(1);
		at.setFocusOn(1);
		at.setDirectContact("客服顾问");
		at.setDirectContactPhone("15899564125");
		at.setModifiedBy(666L);
		int i = -1;
		try{
			i = customerService.updateCusAttribute(at);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(i);
	}
	
	//查询会员属性
	@Test
	public void queryCusAttribute(){
		CardUtilBO c = new CardUtilBO();
		c.setCardId("20170801");
		CusAttribute attr = customerService.queryCusAttribute(c);
		System.err.println(JsonUtil.beanToJson(attr));
	}
	
	//查询会员属性等额外信息
	@Test
	public void queryKafkaCusAttr(){
		KafkaCusAttribute cusAttr = customerService.queryKafkaCusAttr("892559434327261184");
		System.err.println(JsonUtil.beanToJson(cusAttr));
		messageSender.sendMessage(KafkaConstant.KFK_OPERATION_UPDATE, cusAttr, "customer_attribute");
	}
	
	//更新会员身份证号码
	@Test
	public void updateIdentificationNoTest(){
		IDUtilBO idUtilBO = new IDUtilBO();
		idUtilBO.setCustomerId("900648984786239488");
		idUtilBO.setIdentificationNo("510921199309224369");
		idUtilBO.setModifiedBy(987654321L);
		
		int info = -1;
		
		try{
			info = customerService.updateIdentificationNo(idUtilBO);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		System.err.println(info);
	}
	
	//查询是否存在该会员
	@Test
	public void queryCustomerExistTest(){
		List<Integer> orgs = RoleUtil.getOrgIdsLine(dep, restTemplate, "04512446358848");
		QueryCusBO bo = new QueryCusBO();
		bo.setMobile("13202636520");
		bo.setOrgs(orgs);
		CusExist cus = customerService.queryCustomerExist(bo);
		System.err.println(JsonUtil.beanToJson(cus));
	}
	
	@Test
	public void queryFreeCustomerListTest(){
		FreeCusBO fb = new FreeCusBO();
		fb.setPage(1);
		fb.setPageSize(3);
		String userId = "0459026525836";
		fb.setUserId(userId);
		List<Integer> orgIds = RoleUtil.getDepartMentOrgId(dep, restTemplate, userId);
		if(ChkUtil.isEmpty(orgIds)){
			orgIds.add(-1);
		}else if(orgIds.size() <= 0){
			orgIds.add(-1);
		}
//		fb.setOrgIds(orgIds);
		ListVO custList = null;
		custList = customerService.queryFreeCustomerList(fb);
		System.err.println(JsonUtil.beanToJson(custList));
		
	}
	
	@Test
	public void reassignCustomerTest(){
		ReassignCusBO reb = new ReassignCusBO();
		List<String> list = new ArrayList<String>();
		list.add("123456");
		list.add("123467");
		list.add("1236598");
		reb.setCardList(list);
		reb.setModifiedBy(666666L);
		reb.setServId("999999");
		reb.setServName("999999");
		try{
			customerService.reassignCustomer(reb);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void queryCusByIdsTest(){
		IdsBO idsBO = new IdsBO();
		List<String> ids = new ArrayList<String>();
		ids.add("900648984786239488");
		ids.add("900650337277313024");
		idsBO.setIds(ids);
		List<CusByIdsVO> customerList = customerService.queryCusByIds(idsBO);
		System.err.println(JsonUtil.beanToJson(customerList));
	}
	
	@Test
	public void queryCustomerTest(){
		AssignCusBO acb = new AssignCusBO();
		acb.setPage(1);
		acb.setPageSize(5);
		
		String userId = "0459026525836";
		List<Integer> orgs = new ArrayList<>();//[11085644,11085645,11085646,11085647,30515630,11086563]
		orgs.add(11085644);
		orgs.add(11085645);
		orgs.add(11085646);
		orgs.add(11085647);
		orgs.add(30515630);
		
		acb.setUserId(userId);
		acb.setOrgs(orgs);
		
		ListVO cusList = customerService.queryCustomerList(acb);
		
		System.err.println(JsonUtil.beanToJson(cusList));
	}
	
	@Test
	public void queryCusInfosByIdsTest(){
		IdsBO idsBO = new IdsBO();
		List<String> ids = new ArrayList<String>();
		ids.add("900648984786239488");
		ids.add("900650337277313024");
		idsBO.setIds(ids);
		List<CusInfoByIdsVO> customerList = customerService.queryCusInfosByIds(idsBO);
		System.err.println(JsonUtil.beanToJson(customerList));
	}
	
	@Test
	public void queryCustomer2Test(){
		ContractBO contractBO = new ContractBO();
		contractBO.setId("610428198601085723");
		contractBO.setReq("12345689");
		ResponseDTO rd = new ResponseDTO();
		List<CustomerDTO> cusList = customerService.queryCustomer(contractBO);
		rd.setReq(contractBO.getReq());
		rd.setRspd("987654321");
		rd.setCode(0);
		rd.setCusList(cusList);
		System.err.println(JsonUtil.beanToJson(rd));
	}
	
	@Test
	public void getOrgIdByUserId(){
		String userId = "050441615520380291";//04494011285133   072441065530984887
//		String userId = "0459026525836";
//		String userId = "666";
		CardListUtilBO cardListUtilBO = new CardListUtilBO();
		List<Integer> orgs = RoleUtil.getDepartMentOrgId(dep, restTemplate, userId);
		cardListUtilBO.setOrgs(orgs);
		if(ChkUtil.isEmpty(orgs)){
			List<String> userIds = RoleUtil.getDepartMentUser(dep, restTemplate, userId);
			cardListUtilBO.setUserIds(userIds);
		}else if(orgs.size() <= 0){
			List<String> userIds = RoleUtil.getDepartMentUser(dep, restTemplate, userId);
			cardListUtilBO.setUserIds(userIds);
		}
		System.err.println(JsonUtil.beanToJson(cardListUtilBO));
	}

	@Test
	public void DictTest(){
		//1.查询全部字典
//		List<Dict> dictList = null;
//		try{
//			dictList = dictService.queryAllDict();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		System.err.println(JsonUtil.beanToJson(dictList));
		
		//2.根据parantId查询字典
//		Long parentId = 4L;
//		List<DictOptionVO> dictList2 = null;
//		dictList2 = dictService.queryDictByParentId(parentId);
//		System.err.println(JsonUtil.beanToJson(dictList2));
//		
		//3.新增字典
//		InsertDictBO tb = new InsertDictBO();
//		tb.setParentId(4L);
//		tb.setTitleName("单元测试22");
//		tb.setCreatedBy(123456L);
//		DictAdd info = null;
//		info = dictService.insertDict(tb);
//		System.err.println(JsonUtil.beanToJson(info));
//		
		//4.更新字典
//		UpdateDictBO ub = new UpdateDictBO();
//		ub.setId(35L);
//		ub.setTitleName("山丘");
//		ub.setModifiedBy(789456L);
//		int info2 = -1;
//		info2 = dictService.updateDict(ub);
//		System.err.println(info2);
		
		//5.删除字典
		DeleteDictBO db = new DeleteDictBO();
		db.setId(36L);
		db.setDeletedBy(123465789L);
		int info3 = -1;
		info3 = dictService.deleteDict(db);
		System.err.println(info3);
	}
	
	@Test
	public void getOrgIdsLine(){
		String userId = "03564268423981";//04512558574944
		List<Integer> orgs = RoleUtil.getOrgIdsLine(dep, restTemplate, userId);
		System.err.println(JsonUtil.beanToJson(orgs));
	}
	
	@Test
	public void getorgNameTest(){
		String servString  = (String) redisClient.getHash(RedisConstant.USER_ROOT + "04512734426104", RedisConstant.USER_GROUP);
		User serUser = JsonUtil.jsonToBean(servString,User.class);
		String fullOrgName = serUser.getFullOrgName();
		String[] names = fullOrgName.split("-");
		String orgName = names[1]+names[2];
		for(String s : names){
			System.err.println(s);
		}
		System.err.println(fullOrgName);
		System.err.println(JsonUtil.beanToJson(serUser));
	}
	
	@Test
	public void getDeptOrgsTest(){
//		List<String> orgids = RoleUtil.getParentDept(dep, restTemplate, "10941537");
//		System.err.println(JsonUtil.beanToJson(orgids));
		//"10941537","10886905","10886871","35832622","35838596","18577762","34580956","10726834","1"
		
		//客服一部  10941537 南京中山北路客服中心	10886905城市督导（陆岭、季侠森   南京）	10886871区域总监（梁岩  南京）	35832622
		//项目副总	35838596爱福家	18577762 老龄金融	34580956 董事长	10726834 华晚投资管理（北京）有限公司	1
		String orgId = "35838596";
		List<Integer> orgs = RoleUtil.getDeptOrgs(dep, restTemplate, orgId);
		System.err.println(JsonUtil.beanToJson(orgs));
	}
}
