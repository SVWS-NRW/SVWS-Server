<template>
	<Teleport defer to=".svws-ui-header--actions">
		<svws-ui-button type="secondary" @click="print"><span class="icon i-ri-printer-line" />Drucken</svws-ui-button>
		<svws-ui-modal-hilfe> <hilfe-klassen-stundenplan /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page--content page--content--flex h-full">
		<template v-if="stundenplan === undefined">
			<div class="svws-ui-empty">
				<span class="icon i-ri-calendar-event-line" />
				<span>Derzeit liegt kein Stundenplan<br>für diesen Lernabschnitt vor.</span>
			</div>
		</template>
		<template v-else>
			<stundenplan-auswahl :stundenplan :map-stundenplaene :goto-stundenplan :goto-wochentyp :goto-kalenderwoche :manager :wochentyp :kalenderwoche :ganzer-stundenplan :set-ganzer-stundenplan />
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
