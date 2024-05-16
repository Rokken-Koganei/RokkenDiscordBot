echo "+-----------------------+"
echo "+ Input Discord User ID +"
echo "+-----------------------+"
read -p ">> " INPUT_STR
echo "your input value is [$INPUT_STR]"

read -p "(y/n): " CONFIRM
case "$CONFIRM" in
	[yY])
		REG_STR="/"$INPUT_STR"/d"
		echo "$REG_STR"
		sed -i -e "$REG_STR" migration.txt
		echo "Deleted User ID"
	;;
        [nN])
		echo "Cancelled."
	;;
esac

echo "---- Diff is here ----"
diff migration.txt{,.20240516.bak}

read -p "press ENTER to exit..."
