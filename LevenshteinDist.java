public class LevenshteinDist{
	public static int execute(String a, String b) {
		int matRek[][]=new int[a.length()+1][b.length()+1];
		matRek[0][0]=0;
		
		for(int i=1;i<matRek.length;i++)
			matRek[i][0]=i;
		for(int j=1;j<matRek[0].length;j++)
			matRek[0][j]=j;
		
		for (int i=1;i<matRek.length;i++){
			for (int j=1;j<matRek[0].length;j++){
				int resultSet[]=new int[3];
				if (a.charAt(i-1)==b.charAt(j-1))
					resultSet[0]=matRek[i-1][j-1];
				else{
					resultSet[0]=matRek[i-1][j-1]+1;
				}
				resultSet[1]=matRek[i][j-1]+1;
				resultSet[2]=matRek[i-1][j]+1;
				matRek[i][j]=min(resultSet);	
				
			}
		}	
		return matRek[a.length()][b.length()];			
	}
	private static int min(int[] res){
		int min=res[0];
		for (int i=1;i<res.length;i++)
			if (res[i]<min)
				min=res[i];
		return min;
	};
}