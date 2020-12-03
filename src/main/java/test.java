
interface MyCallback{
	void callback(int money);
}

class MoneyChange{
	int money = 10000;
	
	public void send(MyCallback myCallback) {
		
		//새로운 스레드 실행
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					money = money+20000;
					myCallback.callback(money);
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
				
			}
		}).start();
	}
}

public class test {
	public static void main(String[] args) {
		MoneyChange m =new MoneyChange();
		 m.send(new MyCallback() {
			
			@Override
			public void callback(int money) {
				// TODO Auto-generated method stub
				System.out.println(money);
			}
		});
		
	}
}
