package xu.ye.view.ui;

import java.util.Observable;


public class Watched  extends Observable
{
	private String data = "";

	public String retrieveData()
	{
		return data;
	}

	public void changeData(String data)
	{
		if ( !this.data.equals( data) )
		{
			this.data = data;
			setChanged();//将Observable的changed = true
		}
		/**
		 * 调用观察者的update方法
		 * 方法源代码：
		 * 
         Object[] arrLocal;
		 synchronized (this) {
	    
	     if (!changed)
                 return;
             arrLocal = obs.toArray();
             clearChanged();
         }
         for (int i = arrLocal.length-1; i>=0; i--)
             ((Observer)arrLocal[i]).update(this, arg);
    	 }
		 * 
		 * 
		 */
		notifyObservers();
	}
}
