<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-button type="secondary" @click="print"><i-ri-printer-line />Drucken</svws-ui-button>
		<svws-ui-modal-hilfe> <hilfe-lehrer-stundenplan /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page--content page--content--flex">
		<template v-if="stundenplan === undefined">
			<div class="svws-ui-empty">
				<i-ri-calendar-event-line />
				<span>Derzeit liegt kein Stundenplan<br>für diesen Lernabschnitt vor.</span>
			</div>
		</template>
		<template v-else>
			<stundenplan-auswahl :stundenplan="stundenplan" :map-stundenplaene="mapStundenplaene" :goto-stundenplan="gotoStundenplan" :goto-wochentyp="gotoWochentyp"
				:goto-kalenderwoche="gotoKalenderwoche" :manager="manager" :wochentyp="wochentyp" :kalenderwoche="kalenderwoche" />
			<router-view :key="$route.hash" />
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanAuswahlProps } from "@comp";
	import { ref, onMounted } from "vue";

	defineProps<StundenplanAuswahlProps>();

	const print = () => {
		window.print();
	};

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>

<style lang="postcss">
.svws-ui-empty {
	@apply flex flex-col justify-center items-center min-h-full w-full flex-grow;
	@apply text-headline-md text-black/50 dark:text-white/50 text-center;

	svg {
		@apply w-full h-1/5 text-light dark:text-white/5 mb-5;
		max-width: 20vw;
	}
}
</style>
