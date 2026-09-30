<template>
	<div class="ui-grid-popover">
		<div class="flex flex-col w-fit gap-2">
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item" @click="tap(1)">1</span>
				<span class="popover-touch-item" @click="tap(2)">2</span>
				<span class="popover-touch-item" @click="tap(3)">3</span>
			</div>
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item" @click="tap(4)">4</span>
				<span class="popover-touch-item" @click="tap(5)">5</span>
				<span class="popover-touch-item" @click="tap(6)">6</span>
			</div>
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item" @click="tap(7)">7</span>
				<span class="popover-touch-item" @click="tap(8)">8</span>
				<span class="popover-touch-item" @click="tap(9)">9</span>
			</div>
			<div class="flex justify-between gap-2">
				<span class="popover-touch-item extra-button" @click="tap(-1)"><span class="icon-xl i-ri-delete-back-2-line" /></span>
				<span class="popover-touch-item" @click="tap(0)">0</span>
				<span class="popover-touch-item extra-button" @click="emit('close')"><span class="icon-xl i-ri-check-line" /></span>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	const fs = defineModel<number | null>({ default: null });
	const emit = defineEmits<{
		close: [val: void];
	}>();

	function tap(val: number) {
		let str = fs.value?.toString() ?? "";
		if (val >= 0) {
			str = str + val.toString();
		} else if (str.length > 1) {
			str = str.slice(0, -1);
		} else {
			str = "0";
		}
		const num = Number.parseInt(str);
		if ((num < 0) || (num > 999)) {
			return;
		}
		fs.value = num;
	}

</script>
