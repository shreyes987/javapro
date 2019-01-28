	
import java.util.*;
import java.io.*;
import javax.swing.*;

//******************************************************************
// exception class

class ExceptionQuine extends Exception
{
  ExceptionQuine(String str)
  {
    super(str);
    //JOptionPane.showMessageDialog(null,"you idiot please enter correct equation","Error",JOptionPane.ERROR_MESSAGE);
  }
}
//*****************************************************************
/*class used to find out min terms and combineterms
 */
class MinTerm
{

  //external data representation
  public static final char NOT_CH = '0';
  public static final char SET_CH = '1';
  public static final char ANY_CH = '*';

 //internal data representation
  protected static final int NOT =  0;
  protected static final int SET =  1;
  protected static final int ANY = -1;

  // attribute
  protected int count;
  protected int term[];


  public MinTerm(String str)
  {
    term = new int[str.length()];
    for (int i = 0; i < str.length(); i++)
    {
      switch (str.charAt(i))
      {
        case NOT_CH: term[count++] = NOT; break;
        case SET_CH: term[count++] = SET; break;
        case ANY_CH: term[count++] = ANY; break;
      }
    }
  }
//**************************************************************
  // converting in string
  public String toString()
  {
    StringBuffer buf = new StringBuffer(count);
    for (int i = 0; i < count; i++)
    {
      switch (term[i])
      {
        case NOT: buf.append(NOT_CH); break;
        case SET: buf.append(SET_CH); break;
        case ANY: buf.append(ANY_CH); break;
      }
    }
    return buf.toString();
  }

//****************************************************************
  public boolean isSame(MinTerm a) throws ExceptionQuine
  {
    if (count != a.count) throw new ExceptionQuine("MinTerm::isSame()");
    for (int i = 0; i < count; i++)
    {
      if (term[i] != a.term[i]) return false;
    }
    return true;
  }
//****************************************************************

  public int resolutionCount(MinTerm a) throws ExceptionQuine
  {
    if (count != a.count) throw new ExceptionQuine("MinTerm::resolutionCount()");
    int resCount = 0;
    for (int i = 0; i < count; i++)
    {
      if (term[i] != a.term[i]) resCount++;
    }
    return resCount;
  }
//****************************************************************
   public int resolutionPos(MinTerm a) throws ExceptionQuine
   {
    if (count != a.count) throw new ExceptionQuine("MinTerm::resoutionPos()");
    for (int i = 0; i < count; i++)
    {
      if (term[i] != a.term[i]) return i;
    }
    return -1;
  }
//****************************************************************
   public static MinTerm combine(MinTerm a, MinTerm b) throws ExceptionQuine
   {
    if (a.count != b.count) throw new ExceptionQuine("MinTerm::combine()");
    StringBuffer buf = new StringBuffer(a.count);
    for (int i = 0; i < a.count; i++)
    {
      if (a.term[i] != b.term[i]) buf.append(ANY_CH);
      else buf.append(a.toString().charAt(i));
    }
    return new MinTerm(buf.toString());
  }

}
//************Class end*******************************************

class Quine
{

  protected static final int MAX_TERMS = 0xffff;

  // attribute
  public MinTerm[] terms = new MinTerm[MAX_TERMS];
  //static{ System.out.print(MAX_TERMS);}
  public int count = 0;
  MinTerm[] reducedTerms;
  public String[] reducedTermsCopy= new String[MAX_TERMS];
  public String[] reducedTermsCopy2;
  public String[] reducedTermsLast;
  public static int copyCounter=0;
  int totalReduced;
  int ct3[]=new int[20];
  int sail,sail1,sail2;
  Standard stand = new Standard();
  String[] Eqn;

  //min-term
  public void addTerm(String str) throws ExceptionQuine
  {
    if (count == MAX_TERMS) throw new ExceptionQuine("Quine::addTerm()");
    terms[count++] = new MinTerm(str);
  }
//****************************************************************
  // converting in string
  public String toString()
  {
    
    StringBuffer buf = new StringBuffer();
    
    for (int i = 0; i < count; i++)
    {
      reducedTermsCopy[i]=terms[i].toString();
      buf.append(terms[i] + "\n");
    }
	
    return buf.toString();
  }

//******************************************************************

  public boolean hasTerm(MinTerm a) throws ExceptionQuine
  {
    for (int i = 0; i < count; i++)
    {
      if (a.isSame(terms[i])) return true;
    }
    return false;
  }
//*****************************************************************

  public void simplify() throws ExceptionQuine
  {
    while (reduce() > 0);

  }
//****************************************************************
   private int reduce() throws ExceptionQuine
   {

	
    int reducedCount = 0;
    reducedTerms = new MinTerm[MAX_TERMS];
    //reducedTermsCopy = new String[MAX_TERMS];
    boolean used[] = new boolean[MAX_TERMS];


     for (int i = 0; i < count; i++)
     {
      for (int j = i + 1; j < count; j++)
      {

         if (terms[i].resolutionCount(terms[j]) == 1)
        {
          reducedTerms[reducedCount++] = MinTerm.combine(terms[i], terms[j]);
          used[i] = true; used[j] = true;
        }

      }
    }


    int totalReduced = reducedCount;
    for (int i = 0; i < count; i++)
    {
      if (used[i] == false)
      {
        reducedTerms[totalReduced++] = terms[i];
      }
    }

    // initialisation
    count = 0;


    for (int i = 0; i < totalReduced; i++)
    {
      if (!hasTerm(reducedTerms[i]))
        terms[count++] = reducedTerms[i];
    }

	for (int t=0;t<totalReduced;t++)
	{
		reducedTermsCopy[t]=reducedTerms[t].toString();
		//System.out.println(reducedTermsCopy[t]);
	}

	ct3[sail++]=totalReduced;
	//System.out.println(totalReduced);
	//System.out.println(reducedCount);
    
return reducedCount;
}

//****************************************************************

void extracter()
{

	for(int i=0;i<ct3.length;i++)
		if(ct3[i]!=0)
			sail1++;
	for (int t=0;t<reducedTermsCopy.length;t++)
    	if(reducedTermsCopy[t]!=null)
    		copyCounter++;
	reducedTermsCopy2= new String[copyCounter];
	for(int t=0;t<reducedTermsCopy2.length;t++)
		reducedTermsCopy2[t]=reducedTermsCopy[t];

	sail2=ct3[sail1-1];
	reducedTermsLast=new String[sail2];
	for(int i=0;i<sail2;i++)
		reducedTermsLast[i]=reducedTermsCopy2[i];



}
 //***************************************************************
void conToEqn(Standard stand)
{
	Eqn=new String[reducedTermsLast.length];
	for (int i=0;i<reducedTermsLast.length;i++)
	{
		StringBuffer ct5= new StringBuffer();
		String ct4=reducedTermsLast[i];
		for (int t=0;t<ct4.length();t++)
		{

			if((int)ct4.charAt(t)==49)
				ct5.append(stand.masterArray[t]);
			else if((int)ct4.charAt(t)==48)
				ct5.append(stand.masterArray[t].toLowerCase());

		}
		ct4=ct5.toString();
		Eqn[i]=ct4;

	}

}


}
//**************************************************************
 class Standard //throws ExceptionQuine

{

	String masterArray[]=new String[65535];
	public char ma[]=new char[65535];
    String key1[]=new String[65535];
	int a3[]=new int[65535];
	boolean flag=false;
	int err=0;
	String Std=new String();
	//static int count=1;
	int count=1;
	//static int count1=0;
	int count1=0;
	//static int k3=0;
	int k3=0;
	int no3=1;
	int x;
	//static int plus=0;
	int plus=0;
	//static int size1;
	int size1;
	 int x3=0;
     int d3=0;
    int no;
    char abc;

	void compare(char ch,int Size)
	{
		for(int i=0;i<Size;i++)
		{

			int am=(int)ma[i]+32;
			int c=(int)ch-32;
			if((int)ch==43)
			plus++;

			if((ch==ma[i])||(c==(int)ma[i])||((int)ch==am)||((int)ch==43))
			{
				flag=true;

				break;
			}
			else
				flag=false;
		}
		if(!flag)
		{

			if ((int)ch>=97)
				ma[count]=(char)(ch-32);
			else
				ma[count]=ch;
			count++;
			flag=false;
		}



	}
	/*void dispArray()
	{
		for(int i=0;i<count;i++)
			System.out.println(masterArray[i]);
	}*/

	void contostring()
	{

	for(int i=0;i<ma.length;i++)
	{

	    Character ob=new Character(ma[i]);
		masterArray[i]=ob.toString();

	}

	}

	void sortmasterArray()
	{


		for(int i=0;i<count;i++)
 		{
 			String temp=masterArray[i];
 			for(int j=i+1;j<count;j++)
 			{
 				if(masterArray[i].compareTo(masterArray[j])>0)
 				{
 					temp=masterArray[i];
 					masterArray[i]=masterArray[j];
 					masterArray[j]=temp;
 				}
 			}
 		}
 	}

//*******************************************************************
	void get(String Sr)
	{

		String tempkey[]=new String[plus+1];
		String save[]=new String[plus+1];
		StringTokenizer st=new StringTokenizer(Sr,"+");
		int k=0,m=0,n=0;
			count1=0;
			x=0;

		StringBuffer Sr1;

		outer:for(int i=0;i<tempkey.length;i++)
		{

			tempkey[i]=st.nextToken();
			Sr1=new StringBuffer(tempkey[i]);
			int size=Sr1.length();
			o1:for(int j=0;j<size;j++)
			{


				for(int k1=size-1;k1>0;k1--)
				{
					if(j==k1)
					continue o1;
					else
					{
					 if(Sr1.charAt(j)==Sr1.charAt(k1))
					 {
						Sr1.deleteCharAt(k1);
						size--;

					  }

				   }
		        }

		    }

		   tempkey[i]=Sr1.toString();
			}
			/*for(int i=0;i<tempkey.length;i++)
				System.out.println(tempkey[i]);*/
		   int counter=0;
		   for(int i=0;i<tempkey.length;i++)
		   {
		   	if(tempkey[i]==null)
		   	counter++;
		   	else
		   		{
		   		if(counter==0)
		   		continue;
		   		else
		   		tempkey[i-counter]=tempkey[i];
		   		}
		   }
		   String key[]=new String[tempkey.length-counter];
		   for(int i=0;i<key.length;i++)
		   {
		   	key[i]=tempkey[i];
		   }
		int cn=0;

		for(int i=0;i<key.length;i++)
		{
			 x=0;
			String s=key[i];
			if(key[i]==null)
				{
					save[i]=null;
					continue;
				}

			outer1:for(int j=0;j<count;j++)
				{
					String am=masterArray[j];

				outer2:for(k=0;k<s.length();k++)
					{	int n1=(int)s.charAt(k);
						int c=(n1-32);
						if(((am.charAt(0)==s.charAt(k))||((int)am.charAt(0)==c)))
							{
								cn++;
								if(cn==masterArray.length)
								{
									save[count1]=null;

								}
								continue outer1;
						}
						else
				            continue outer2;


				    }
						x++;
						if(x==1)
							save[count1]=am;
                        else
							save[count1]=save[count1]+am;
				}

				count1++;

			}

		/*for(int i=0;i<save.length;i++)
		{
		System.out.println(save[i]);
		}*/
	newcopy(key,save);



}	//******************************************************************



	void newcopy(String key[],String save[])

	{

		k3=0;
		for(int i=0;i<key.length;i++)
		{

			if(save[i]==null)
			{
			 key1[k3]=key[i];
				k3++;
				continue;
			}
			no3=1;
			String S=key[i];
			int size;
			size=count-S.length();

			for(int j=0;j<size;j++)
				no3=2*no3;
				a3[i]=no3;

				if(k3==0)
			{
				for(int l=k3;l<no3;l++,k3++)
				key1[l]=key[i];


			}
			else
			{
				int k4=k3;
				for(int l=k3;l<k4+no3;l++)
				{key1[l]=key[i];
					 k3++;

				}




		}
		/*for(int i1=0;i1<k3;i1++)
		{
			System.out.println(key1[i1]);

		}*/
	}
	newstd(key,key1,save);
	}

//*******************************************************************
String newkey[];
void newstd(String key[],String key1[],String save[])
	{
		
		newkey=new String[k3];
		size1=newkey.length;
		for(int i=0;i<k3;i++)
		newkey[i]=key1[i];
		d3=0;
		for(int i=0;i<key.length;i++)
		{

			String S=save[i];
			    if(a3[i]==0)
			    {
			     d3++;
			     continue;
			 }
			    no=a3[i]/2;
				x3=no;
				int p=1;


			for(int j=0;j<S.length();j++)
			{


                if(j>0)
				{
					d3=d3-a3[i];

					x3=x3/2;
					no=x3;
				}



				for(int k=0;k<p;k++)
				{


					for(int l=0;l<no;l++)
					{
						newkey[d3]=newkey[d3]+S.charAt(j);
						d3++;



                    }
					for(int m=0;m<no;m++)
					{
						int n=(int)S.charAt(j);
						char c=(char)(n+32);
						newkey[d3]=newkey[d3]+c;
						d3++;

					}
			    }
				p=p*2;

		}
	


		}

int m=0;
		for (int i=0;i<newkey.length;i++)
		{
			String S=newkey[i];
			char arrS[]= new char[S.length()];
			for(int j=0;j<S.length();j++)
			{

				arrS[j]=S.charAt(j);


			}
			Arrays.sort(arrS);

			for(int k=0;k<S.length();k++)
			{
				Character ob=new Character(arrS[k]);
				if(m==0)
				{
					newkey[i]=ob.toString();
					m++;
				}
				else
				newkey[i]=newkey[i]+ob.toString();
			}
			m=0;

		}
		xSort();
		int cn=0;
		for(int i=0;i<newkey.length;i++)
		{
			for(int j=i+1;j<newkey.length;j++)
			{
				if(newkey[i].equals(newkey[j]))
				{
					newkey[j]="";

				}
			}
		}
		for(int i=0;i<newkey.length;i++)
		{
			if(newkey[i]=="")
			{
				cn++;
			    size1--;
			}
			else
			{
				if(cn==0)
				newkey[i-cn]=newkey[i];
				else
				{
					newkey[i-cn]=newkey[i];
				    newkey[i]="";
				}
			}
		}
		/*System.out.println("The standarised eqn is");
	    for(int i=0;i<size1;i++)
		{
		   if(i==size1-1)
		   {
    		System.out.print(newkey[i]);
    		//buff1.append(newkey[i]);
    	}
    		else
    		{
    		System.out.print(newkey[i]+"+");
    		//buff1.append(newkey[i]+"+");
    	}
    	//Std=buff1.toString();
		}*/
	//System.out.println("");
	contobinary(newkey);

}
//*********************************************************************
void xSort()
	{
		for(int r=0;r<newkey.length;r++)
		{
			String st = new String(newkey[r]);
			newkey[r]=sorter(st);

		}

	}

	String sorter(String st)
	{
		StringBuffer J=new StringBuffer(st.length());
		for (int q=0;q<st.length();q++)
		{
			char cd=looker(masterArray[q].charAt(0),st);
			J.append(cd);
		}
		return J.toString();
	}

	char looker(char var,String st)
	{
		int x,cd,dc;
		cd=(int)var;
		dc=(int)var+32;

		for (x=0;x<st.length();x++)
		{
			if ((int)st.charAt (x)==cd)
			{
				abc=(char)cd;
				break;
			}
			else if((int)st.charAt (x)==dc)
			{
				abc=(char)dc;
				break;
			}
		}

		return abc;
	}
//*********************************************************************
String str[];

void contobinary(String newkey[])
{
	int i,j,m;
	m=0;
	Quine quine = new Quine();
	str=new String[size1];
	for(i=0;i<str.length;i++)
	{
	 String s=newkey[i];
     for(j=0;j<s.length();j++)
		{

			if(((int)s.charAt(j)>=65)&&((int)s.charAt(j)<=90))
			{
				if(m==0)
				{
					str[i]="1";
					m++;
				}
				else
				str[i]=str[i]+"1";
			}
			else
			if(((int)s.charAt(j)>=97)&&((int)s.charAt(j)<=122))
			{
				if(m==0)
				{
					str[i]="0";
					m++;
				}
				else
				str[i]=str[i]+"0";
			}

		}
		m=0;
	}
	/*for(i=0;i<str.length;i++)
	{
		System.out.println(str[i]);
	}*/
	}
	String reduce1(String Sr)

	{
		int p=0;
		for(int i=0;i<Sr.length();i++)
		{
			if((int)Sr.charAt(i)==43)
				p++;
		}
		String temp1[]=new String[p+1];
		StringTokenizer st=new StringTokenizer(Sr,"+");
		for(int i=0;i<temp1.length;i++)
			temp1[i]=st.nextToken();
		int size=temp1.length;
		outer:for(int i=0;i<temp1.length;i++)
		{
			String S=temp1[i];

			for(int j=0;j<S.length();j++)
			{
				for(int k=j+1;k<S.length();k++)
				{
					int a=(int)(S.charAt(j));
					int c=a+32;
					int b=(int)(S.charAt(k));
					int d=b+32;


					if(((char)c==(S.charAt(k)))||((S.charAt(j))==(char)d))
					{

						temp1[i]=null;
						continue outer;

					}
				}
			}
		}
		int cn=0;
		try
		{
			for(int i=0;i<temp1.length;i++)
		{
			if(temp1[i]==null)
			{
				cn++;
			    size--;
			}
			else

			{
				if(cn==0)
				temp1[i-cn]=temp1[i];
				else
				{
					temp1[i-cn]=temp1[i];
				    temp1[i]=null;
				}
			}

		}

		StringBuffer Sr1=new StringBuffer(temp1[0]);

		for(int i=1;i<size;i++)
		{
					Sr1.append("+").append(temp1[i]);


		}
		Sr=Sr1.toString();

	}
	catch(NullPointerException e)
	{
		err++;
		JOptionPane.showMessageDialog(null,"The value is 0","Reduced Value",JOptionPane.ERROR_MESSAGE);
		return null;
	}
	
	return(Sr);
}
}

	//**************************************************************
public class varFinder1 

{
	Standard C1;
	public String Sz;
	public String sz2;
	//xLogicCircuitsFrame xlc=new xLogicCircuitsFrame();
	//static BufferedReader in;
	//public static void main(String ar[]) throws Exception
	public String eqnReducer(String Sr) throws Exception
	{


	/*	do {*/
		Standard C=new Standard();
		Quine quine = new Quine();
		StringBuffer buff = new StringBuffer();
		/*String  ans="exit";*/

		//Quine mt=new Quine();
		try
		{
		/*System.out.println("Enter the equation");
		 in=new BufferedReader(new InputStreamReader(System.in));
		String Sr=in.readLine();*/
		Sr=C.reduce1(Sr);

		char a;
		int Size=Sr.length();
	    if(((int)Sr.charAt(0)>=97)&&((int)Sr.charAt(0)<=122))
	    {
	    	int c=(int)Sr.charAt(0)-32;
	    C.ma[0]=(char)c;
	    }
	    else
	    C.ma[0]=Sr.charAt(0);

		for(int i=0;i<Size;i++)
		{
			a=Sr.charAt(i);
			if((((int)a>=65)&&((int)a<=91)) || (((int)a>=97)&&((int)a<=122)) || ((int)a==43))
			C.compare(a,Size);
			else
			{
			JOptionPane.showMessageDialog(null,"Do not enter special characters","Error",JOptionPane.ERROR_MESSAGE);
			return null;
			}
		}

		C.contostring();
		C.sortmasterArray();
		//C.dispArray();
		C.get(Sr);
		setSize(C.size1,C.count);
		}catch(Exception e){JOptionPane.showMessageDialog(null,"Please enter proper SOP equation","Error",JOptionPane.ERROR_MESSAGE);}
		
		//start of Quine McCluskey
		for (int b=0;b<C.str.length;b++)
  		{
  			quine.addTerm(C.str[b]);
    	}

    	quine.simplify();
    	//quine.extracter();
    	/*for (int t=0;t<quine.totalReduced;t++)
			System.out.println(quine.reducedTermsCopy[t]);*/
		//System.out.println(quine.totalReduced);
    	System.out.println("The Reduced Eq is");
    	//mt=quine;
    	//bifor (int t=0;
    	//System.out.print(quine);
    	
    	quine.extracter();
    	quine.conToEqn (C);
    	int t;
    	for (t=0;t<quine.Eqn.length;t++)
    	{
    		if(t==quine.Eqn.length-1)
    		{
    			System.out.print(quine.Eqn[t]);
    			buff.append(quine.Eqn[t]);
    		}
    		else
    		{
    			System.out.print(quine.Eqn[t]+"+");
    			buff.append(quine.Eqn[t]+"+");
    		}
        }
    
      //  System.out.println("press exit to exit ");

    //}while( in.readLine().compareToIgnoreCase("exit")!=0);

    	//System.out.println(quine.sail2);
    	
    	//if(((((int)Sr.charAt(0)>=65)&&((int)Sr.charAt(0)<=91))&&(((int)Sr.charAt(2)>=97)&&((int)Sr.charAt(2)<=122)))&&((int)Sr.charAt(1)==43))
        // return "1";
        //if(((((int)Sr.charAt(0)>=97)&&((int)Sr.charAt(0)<=122))&&(((int)Sr.charAt(2)>=65)&&((int)Sr.charAt(2)<=91)))&&((int)Sr.charAt(1)==43))
    	//return "1";
    	
    	
    	System.out.println("value"+buff.toString());
    	return buff.toString();
    	
		
		//return buff.toString();	
	}

	public String viewStd(String Sr)
	{
		
		C1=new Standard();
		StringBuffer buff1=new StringBuffer();
		try{
		Sr=C1.reduce1(Sr);
		char a;
		int Size=Sr.length();
	    if(((int)Sr.charAt(0)>=97)&&((int)Sr.charAt(0)<=122))
	    {
	    	int c=(int)Sr.charAt(0)-32;
	    C1.ma[0]=(char)c;
	    }
	    else
	    C1.ma[0]=Sr.charAt(0);

		for(int i=0;i<Size;i++)
		{
			a=Sr.charAt(i);
			if((((int)a>=65)&&((int)a<=91)) || (((int)a>=97)&&((int)a<=122)) || ((int)a==43))
			C1.compare(a,Size);
			else
			{
			JOptionPane.showMessageDialog(null,"Do not enter special characters","Error",JOptionPane.ERROR_MESSAGE);
			return null;	
			}
		}

		C1.contostring();
		C1.sortmasterArray();
		C1.get(Sr);
	}catch(Exception e){JOptionPane.showMessageDialog(null,"Please enter proper SOP equation","Error",JOptionPane.ERROR_MESSAGE);}
		int size=C1.newkey.length;
		System.out.println ("The Standard form is ");
		//setSize(C1.size1);
		for(int i=0;i<C1.size1;i++)
		{
		   if(i==C1.size1-1)
		   {
    			System.out.print(C1.newkey[i]);
    			buff1.append(C1.newkey[i]);
    		}
    		else
    		{
    			System.out.print(C1.newkey[i]+"+");
    			buff1.append(C1.newkey[i]+"+");
    		}
    	}
    	
    	return buff1.toString();
	}
	void setSize(int sz,int cn)
	{
		StringBuffer Sz1=new StringBuffer();
		
		StringBuffer Sz2=new StringBuffer();
		try{
		Sz=new String();
		Sz2.append(cn);
		Sz1.append (sz);
		Sz=Sz1.toString();
		sz2=Sz2.toString();
	}catch(Exception e){System.out.println("bgjkfj");}
	}
	/*public int getSize()
	{

		return
	}*/

}

