
<template>
	<div v-if="visible" class="space-y-3">
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_1 />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_6 />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_2 :data-faecher="dataFaecher" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_3 :data-faecher="dataFaecher" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_7 :data-faecher="dataFaecher" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_8 :data-faecher="dataFaecher" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_4 :data-faecher="dataFaecher" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_5 :data-faecher="dataFaecher" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_9 />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from 'vue';
	import { DataGostFaecher } from '~/apps/gost/DataGostFaecher';

	const { dataFaecher } = defineProps<{
		dataFaecher: DataGostFaecher;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return true;
	});

</script>
