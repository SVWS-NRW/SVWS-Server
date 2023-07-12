<template>
	<div class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<div class="flex-grow">
			<s-gost-fachwahlen-uebersicht :fachwahlen="fachwahlen" :do-select="doSelect" />
		</div>
		<div class="w-2/5 3xl:w-1/2 min-w-[36rem]">
			<div class="flex flex-col gap-16">
				<router-view />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { GostFachwahlenProps } from "./SGostFachwahlenProps";

	defineProps<GostFachwahlenProps>();

</script>
