package pro3.Service;
 
import java.util.Scanner;

import pro1.Customer_and_Staff.*;
import pro2.Database.User_database;

public class Service {
	
	public final int IS_NONE     = 0;
	public final int IS_CUSTOMER = 1;
	public final int IS_STAFF    = 2;
	public final int BANK_ID   = 1;
	public final int PASSWORD  = 2;
	public final int IDENTITY  = 3;
	public final int PHONE_NO  = 4;
	
	
	//登录界面
	public void Login(Service sev_temp,User_database database) throws Exception{
		Customer temp_c = null;
		Staff temp_s = null;
		boolean login = false;//用来记录登录是否成功
		char select;//用来选择服务  
		Scanner sc = new Scanner(System.in);
		String u_name = null;
		int type = IS_NONE; // determine which type the user is
//=======================================登录部分=====================================
		while(true)
		{
			while(!login)
			{
				System.out.println("========================欢迎来到银行系统========================");
				System.out.println("请您登录！输入账号：");
				sc = new Scanner(System.in);
				u_name = sc.next();
				int x=database.get_order(database,u_name); // 获得用户在arraylist中的序号
				/*   [Has been down]
				 *   TODO: 1. Add some statements for checking the user
				 */
				if( x==-1 )
				{
					System.out.println("账号不存在！");
					break;
				}
				System.out.println("请输入密码:");
				sc = new Scanner(System.in);
				String pass = sc.next();
				if(database.get_cs(u_name)==IS_CUSTOMER){
					/*  [Has been down]
					 *  TODO: 7. Use string.match to determine whether it's a customer or a staff
					 */
					while(!database.customer.get(x).password.equals(pass)){
						if(database.customer.get(x).count == 3){
							System.out.println("连续3次密码输入错误！账号已被冻结！请联系管理员解冻。");// the account will be locked
							break;
						}
						System.out.println("密码输入有误!请重新输入：");
						sc = new Scanner(System.in);
						pass = sc.next();
						/*  [Has been down]
						 *  TODO: 2.  Refactoring for customer.ger(x).count++.     
						 */
						database.customer.get(x).Increase_Password_Wrong_Times();// 错误次数加1
						
					}
					if(database.customer.get(x).count != 3)// if the account is not locked
					/*  [Has been down]
					 *  TODO: 8. Make it able to return to login interface, rather than make the user successfully sign in.
					 */
					{
						database.customer.get(x).count=0;
						temp_c = database.customer.get(x);
						System.out.println("登录成功！");
						login = true;
					}
				}
				else if(database.get_cs(u_name)==IS_STAFF){
					while(!database.staff.get(x).get_password().equals(pass)){
						System.out.println("密码输入有误!请重新输入：");
						sc = new Scanner(System.in);
						pass = sc.next();
					}
					temp_s = database.staff.get(x);
					System.out.println("登录成功！");
					login = true;
				}
			}

	//===========================主体业务部分=================================
			if(temp_c!=null){
				print_user();//打印界面	
				sc = new Scanner(System.in);
				select = sc.next().charAt(0);
				while(select!='g'){
					switch(select){
					case 'a':temp_c.Deposit(sev_temp, temp_c.user_name, database);print_user();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					case 'b':temp_c.Withdraw(sev_temp,temp_c.user_name, database);print_user();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					case 'c':temp_c.Show_balance(sev_temp,temp_c.user_name, database);print_user();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					case 'd':temp_c.Transit(sev_temp,temp_c.user_name, database);print_user();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					case 'e':temp_c.Reset_message(sev_temp,temp_c.user_name, database);print_user();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					case 'f':temp_c.Reset_pass_user(sev_temp,temp_c.user_name, database);print_user();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					default :System.out.println("输入有误！请输入正确的指令：");
					print_user();sc = new Scanner(System.in);select = sc.next().charAt(0);
					}
				}
				if(select=='g')
				{
					temp_s = null;
					login = false;
				}
			}
			else if(temp_s!=null){
				print_staff();
				sc = new Scanner(System.in);
				select = sc.next().charAt(0);
				while(select!='i'){
					switch(select){
					case 'a':temp_s.Create_account(sev_temp,database);print_staff();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					case 'b':temp_s.Delete_account(sev_temp,database);print_staff();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					case 'c':temp_s.Show_user(sev_temp,database, temp_s);print_staff();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					case 'd':temp_s.Deposit(sev_temp, database);print_staff();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					case 'e':temp_s.Withdraw(sev_temp, database);print_staff();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					case 'f':temp_s.Reset_info(sev_temp, database, temp_s);print_staff();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					case 'g':temp_s.Reset_pass(sev_temp, u_name, database,temp_s);print_staff();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					default:System.out.println("请输入正确的指令！");print_staff();sc = new Scanner(System.in);select = sc.next().charAt(0);break;
					}
				}
				if(select=='i')
				{
					temp_c = null;
					login = false;
				}
			}
			temp_c = null;
			temp_s = null;
			login = false;//用来记录登录是否成功
			u_name = null;
			type = IS_NONE;
		}
	}
	
	//(a),开户
	public void Create_account(User_database database){
		/*   [Has been down]
		 *   TODO: 3. Refactoring for judgement.   Using int final USERNAME = 1, PASSWORD = 2, ID = 3, etc.
		 */
		System.out.println("输入开户账号，以“600”开头，8位数字");
		Scanner sc = new Scanner(System.in);
		String u_name = sc.next();//输入用户名
		while((check_type(u_name)!=BANK_ID)||(search_account(database,u_name))){
			System.out.println("输入错误！请重新输入");
			sc= new Scanner(System.in);
			u_name = sc.next();
		}
		System.out.println("输入6位数字密码");
		sc = new Scanner(System.in);//输入密码
		String pass = sc.next();
		while(check_type(pass)!=PASSWORD){
			System.out.println("输入错误！请重新输入");
			sc= new Scanner(System.in);
			pass = sc.next();
		}
		System.out.println("输入身份证号");
		sc = new Scanner(System.in);//输入密码
		String id = sc.next();
		while(check_type(id)!=IDENTITY){
			System.out.println("输入错误！请重新输入");
			sc= new Scanner(System.in);
			id = sc.next();
		}
		System.out.println("输入手机号码");//输入手机号
		sc = new Scanner(System.in);
		String tele = sc.next();
		while(check_type(tele)!=PHONE_NO){
			System.out.println("输入错误！请重新输入");
			sc= new Scanner(System.in);
			tele = sc.next();
		}
		System.out.println("输入地址");//输入地址
		sc = new Scanner(System.in);
		String loc = sc.next();
		System.out.println("输入开户金额");//输入开户金额
		sc = new Scanner(System.in);
		double mon = sc.nextDouble();
		Customer e = new Customer (u_name,id,tele,loc,mon,pass);
		database.customer.add(e);
		System.out.println("开户成功！用户名为:"+u_name+"   开户金额："+mon);
	}
	
	
	//(b),销户
	public void Delete_account(User_database database){
		System.out.println("输入身份证号:");
		Scanner sc = new Scanner(System.in);
		sc = new Scanner(System.in);//输入密码
		String id = sc.next();
		int count = 1; // record the times that the user enter his id.
		while(check_type(id)!=IDENTITY){
			/*   [Has been down]
			 *   TODO: 4. The loop may never end. Add some controllers.
			 */
			System.out.println("输入错误！请重新输入");
			sc= new Scanner(System.in);
			id = sc.next();
			count ++;
			if( count==3 )
			{
				System.out.println("连续输错3次，已返回上一级！");
				return;
			}
		}
		System.out.println("输入密码：");
		sc = new Scanner(System.in);//输入密码
		String pass = sc.next();
		int x=database.get_order(id,database);//获得该账号的序列号
		if(database.customer.get(x).password.equals(pass)){
			database.customer.remove(x);
			System.out.println("用户名为"+database.customer.get(x).user_name+"的用户销户成功");
		}
		else
			System.out.println("密码与身份证号不匹配！请检查输入！");
		
	}
	
	//(c),查询
	public void Show_user(String u_name,User_database database,Staff staff) throws Exception{
		int x=database.get_order(database,u_name);//获得该账号的序列号
		if(x==-1)
			System.out.println("未查到该用户！请检查所输入账号是否正确！");
		else{
			if(staff.level<2)//判断权限
				System.out.println("权限不足，无法操作！");
			else{
			System.out.println("用户名："+database.customer.get(x).user_name+
					"    身份证号："+database.customer.get(x).ID+
					"    电话号码："+database.customer.get(x).tele_number+
					"    住址："+database.customer.get(x).location+
					"    余额："+database.customer.get(x).balance);
			for(int i=0;i<database.customer.get(x).money.size();i++)//把所有存款分类都显示出来
				database.customer.get(x).money.get(i).show();
			}
		}
	}
	
	//(d),存款
	public void Deposit(String u_name,User_database database){
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入金额");
		sc = new Scanner(System.in);
		double m = sc.nextDouble();
		int x=database.get_order(database,u_name);//获得该账号的序列号

		System.out.println("请输入存款年限");
		sc = new Scanner(System.in);
		int y = sc.nextInt();
		while(y!=1&&y!=2&&y!=3&&y!=5&&y!=0){
			System.out.println("请输入正确的存款年限（0代表活期存款）!");
			sc = new Scanner(System.in);
			y = sc.nextInt();
		}
		
		User_Money temp = new User_Money(y,m);
		if(y!=0)
			database.customer.get(x).money.add(temp);//存钱
		
		else
			database.customer.get(x).money.get(0).set_money(m);
		database.customer.get(x).Money_flush();//刷新一下钱
		database.customer.get(x).money.get(database.customer.get(x).money.size()-1).show();
	}
	
	//判断金额是否足够
	public boolean money_adequate(String u_name,double m,User_database database){
		boolean w = false;
		int x=database.get_order(database,u_name);
		if(m<=database.customer.get(x).money.get(0).get_money())
			w = true;
		return w;
	}
	
	//(e),取款
	public void Withdraw(String u_name,User_database database){
		int x=database.get_order(database,u_name);//获得该账号的序列号
		System.out.println("请输入金额");
		Scanner sc = new Scanner(System.in);
		double m = sc.nextDouble();
		
		if(money_adequate(u_name,m,database))
			database.customer.get(x).money.get(0).set_money(-m);
		else
			System.out.println("余额不足！");
		database.customer.get(x).Money_flush();//刷新一下钱
	}
	
	//(f),修改下级管理员密码
	public void Reset_info(User_database database,Staff st){
		System.out.println("请输入账号：");
		Scanner sc = new Scanner(System.in);
		String work_id = sc.next();//输入用户名
		while(!work_id.matches("\\d{4}")){
			System.out.println("输入有误!请重新输入!");
			sc = new Scanner(System.in);
			work_id = sc.next();
		}
		
		int x=database.get_order(database,work_id);//获得该账号的序列号
		
		while(x==-1){
			System.out.println("未查到该管理员！请检查所输入账号是否正确！");
			work_id = sc.next();
			x=database.get_order(database,work_id);
		}
		
		boolean t = database.staff.get(x).level < st.level;
		if(!t)
			System.out.println("权限不够，无法修改密码");
		else if(database.staff.get(x).prohibit)
		{
			System.out.println("请输入重置后的6位密码：");
			sc = new Scanner(System.in);
			String pass = sc.next();
			while(check_type(pass)!=PASSWORD){
				System.out.println("输入错误！请重新输入");
				sc= new Scanner(System.in);
				pass = sc.next();
			}
			database.staff.get(x).reset_password(pass);
			System.out.println("密码修改成功！  被修改管理员:"+work_id+"  新秘密："+pass);
		}
		else
			System.out.println("该管理员未被禁用，无法修改密码");
		
	}
	
	//(g)，重置用户密码
	public void Reset_pass(User_database database,Staff st){
		System.out.println("请输入账号：");
		Scanner sc = new Scanner(System.in);
		String u_name = sc.next();//输入用户名
		
		int x=database.get_order(database,u_name);//获得该账号的序列号
		while(x==-1){
			System.out.println("未查到该用户！请检查所输入账号是否正确！");
			u_name = sc.next();
			x=database.get_order(database,u_name);
		}

		boolean t = st.level ==2;
		
		if(!t)
			System.out.println("权限不够，无法修改密码");
		else if(database.customer.get(x).count==3)
		{
			System.out.println("请输入重置后的6位密码：");
			sc = new Scanner(System.in);
			String pass = sc.next();
			while(check_type(pass)!=PASSWORD){
				System.out.println("输入错误！请重新输入");
				sc= new Scanner(System.in);
				pass = sc.next();
			}
			database.customer.get(x).reset_password(pass);
			System.out.println("密码修改成功！  被修改用户:"+u_name+"  新秘密："+pass);
		}
		else
			System.out.println("该用户未被禁用，无法修改密码");
	}
	
	//是否查询到该用户
	public boolean search_account(User_database database,String u_name){
		boolean x = false;
		for(int i=0;i<database.customer.size();i++){
			if(u_name.equals(database.customer.get(i).user_name)){
				x = true;break;
			}
		}
		return x;
	}
	
	//查询余额======(c)
	public void Show_balance(String u_name,User_database database){
		int x=database.get_order(database,u_name);//获得该账号的序列号
		for(int i=0;i<database.customer.get(x).money.size();i++)//把所有存款分类都显示出来
			database.customer.get(x).money.get(i).show();
	}
	//转账======(d)
	public void Transit(String u_name2,User_database database){//u_name2是汇出钱的一方
		System.out.println("请输入账号：");
		Scanner sc = new Scanner(System.in);
		String u_name = sc.next();//输入用户名
		int x=database.get_order(database,u_name);//获得该账号的序列号
		int y=database.get_order(database,u_name2);//获得该账号的序列号
		if( x==y ) // if the user wants to transfer money to himself
		{
			System.out.println("错误！收款方和付款方为同一账户！");
			return ;
		}
		while(x==-1){
			System.out.println("未查到该用户！请检查所输入账号是否正确！");
			u_name = sc.next();
			x=database.get_order(database,u_name);
		}
		System.out.println("请输入金额");
		sc = new Scanner(System.in);
		double m = sc.nextDouble();
		if(money_adequate(u_name2,m,database)){
			User_Money temp = new User_Money(0,m);
			database.customer.get(x).money.get(0).set_money(m);
			database.customer.get(y).money.get(0).set_money(-m);
		}
		else{
			System.out.println("账户余额不足!");
		}
	}
	//修改个人信息======(e)
	public void Reset_message(String u_name,User_database database){
		int x=database.get_order(database,u_name);//获得该账号的序列号
		System.out.println("请输入新的电话号码：");
		Scanner sc = new Scanner(System.in);
		String tele = sc.next();
		while(check_type(tele)!=PHONE_NO){
			System.out.println("输入错误！请重新输入");
			sc= new Scanner(System.in);
			tele = sc.next();
		}
		System.out.println("请输入新的住址：");
		sc = new Scanner(System.in);
		String loc = sc.next();
		database.customer.get(x).reset_message(tele, loc);
		System.out.println("设置成功！");
	}
	//修改密码======(f)
	public void Reset_pass_user(String u_name,User_database database){
			int x=database.get_order(database,u_name);//获得该账号的序列号
		if(database.customer.get(x).count<3)
		{
			System.out.println("请输入旧密码：");
			Scanner sc = new Scanner(System.in);
			String pass = sc.next();
			while(!database.customer.get(x).password.equals(pass)){
				if(database.customer.get(x).count == 3){
					System.out.println("密码连续错误3次，您的账号已被冻结！请联系管理员解冻！");
					break;
				}
				System.out.println("密码输入有误!请重新输入：");
				sc = new Scanner(System.in);
				pass = sc.next();
				database.customer.get(x).count++;//错误次数加1
			}
			database.customer.get(x).count = 0;//重置密码错误次数
			System.out.println("请输入重置后的6位密码：");//第一次输入
			sc = new Scanner(System.in);
			String pass2 = sc.next();
			while(check_type(pass2)!=PASSWORD){
				System.out.println("输入错误！请重新输入");
				sc= new Scanner(System.in);
				pass2 = sc.next();
			}
			
			System.out.println("请再次输入重置后的6位密码：");
			sc = new Scanner(System.in);
			String pass3 = sc.next();
			while(  (check_type(pass3)!=PASSWORD)  ||  (!pass2.equals(pass3))  ){//当输入不是6位数字，或者两次输入密码不相同时
				System.out.println("输入错误！请重新输入");
				sc= new Scanner(System.in);
				pass3 = sc.next();
			}
				database.customer.get(x).reset_password(pass2);
			System.out.println("密码修改成功！  被修改用户:"+u_name+"  新秘密："+pass2);
		}
		if(database.customer.get(x).count == 3)
			System.out.println("密码连续错误3次，您的账号已被冻结！请联系管理员解冻！");
	}
	public void print_user(){
		System.out.println("============选择服务===========");
		System.out.println("存款（a）         取款（b）        查询余额（c）\n转账（d）        修改个人信息（e）        修改密码（f）\n注销（g）");
		System.out.println("============================");	
	}
	public void print_staff(){
		System.out.println("============选择服务===========");
		System.out.println("开户（a）         销户（b）       查询客户（c）\n存款（d）        取款（e）        修改管理员信息（f）\n解冻账户（g）        注销（i）");
		System.out.println("=============================");
	}
	
	private int check_type(String str){
		if(str.matches("600\\d{5}"))
			return BANK_ID;
		if(str.matches("\\d{6}"))
			return PASSWORD;
		if(str.matches("\\d{18}"))
			return IDENTITY;
		if(str.matches("\\d{11}"))
			return PHONE_NO;
		return -1;
	}
}

