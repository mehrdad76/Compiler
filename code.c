//int b;
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
		if (d == 4)
			break;
	}
	// comment1
	return f + b;
}