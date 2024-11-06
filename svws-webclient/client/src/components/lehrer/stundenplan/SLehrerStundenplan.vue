<template>
	<Teleport defer to=".svws-ui-header--actions">
		<svws-ui-button type="secondary" @click="print"><span class="icon i-ri-printer-line" />Drucken</svws-ui-button>
		<svws-ui-modal-hilfe> <hilfe-lehrer-stundenplan /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page--content page--content--flex h-full">
		<template v-if="stundenplan === undefined">
			<div class="svws-ui-empty">
				<span class="icon i-ri-calendar-event-line" />
				<span>Derzeit liegt kein Stundenplan<br>für diesen Lernabschnitt vor.</span>
			</div>
		</template>
		<template v-else>
			<stundenplan-auswahl :stundenplan :map-stundenplaene :goto-stundenplan :goto-wochentyp :goto-kalenderwoche :manager :wochentyp :kalenderwoche :ganzer-stundenplan :set-ganzer-stundenplan
				autofocus />
			<router-view :key="$route.hash" />
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanAuswahlProps } from "@comp";

	defineProps<StundenplanAuswahlProps>();

	const print = () => {
		window.print();
	};

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
