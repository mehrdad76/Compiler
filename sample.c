int b;
int foo(int d,  int e){
	int f;
	void foo2(int k[]) {
		return k[0] + k[1];
	}
	int fff[2];
	fff[0] = d;
	fff[1] = d + 1;
	f = foo2(fff);
	b = e + f;
	while (d < 0) {
		f = f + d;
		d = d - 1;
		if (d ~== 4)
			break;
		else
			continue;
	}
	// comment1
	return f + b;
}
int arr[3];
void main(void){
	int a;
	a = -3 + +11;
	b = 5 * a + foo(a, a);
	arr[1] = b + -3;
	arr[2] = foo(arr[0], arr[1]);
	if (b /* comment2 */ == 3){
		arr[0] = -7;
	}
	else
	{	switch (arr[2]) {
		case 2:
			b = b + 1;
		case 3:
			b = b + 2;
			return;
		case 4:
		{	u = 5;
			b = u * -123;
		break;}
		default:
			b = b - -1;
	}}
	return;
}