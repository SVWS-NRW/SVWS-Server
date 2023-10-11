<template>
	<svws-ui-table clickable :clicked="halbjahr" @update:clicked="gotoHalbjahr" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]"
		:items="GostHalbjahr.values()" />
	<Teleport to=".svws-sub-nav-target" v-if="isMounted">
		<svws-ui-sub-nav>
			<nav class="svws-ui-title-tabs mt-5 ml-3 mb-0">
				<template v-if="children.length === 1">
					<span class="py-1.5 leading-none">{{ children[0].text }}</span>
				</template>
				<template v-else>
					<template v-for="c in children" :key="c.name">
						<button role="link" :class="[ 'svws-ui-title-tab', { 'svws-active': child === c } ]" @click="setChild(c)">
							<span class="absolute left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2">{{ c.text }}</span>
							<span class="font-bold opacity-0">{{ c.text }}</span>
						</button>
					</template>
				</template>
			</nav>
		</svws-ui-sub-nav>
	</Teleport>
</template>

<script setup lang="ts">

	import { GostHalbjahr } from "@core";
	import type { GostKlausurplanungAuswahlProps } from './SGostKlausurplanungAuswahlProps';
	import { ref, onMounted } from 'vue';

	const props = defineProps<GostKlausurplanungAuswahlProps>();

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>
