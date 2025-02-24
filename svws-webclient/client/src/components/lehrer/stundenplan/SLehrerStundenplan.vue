<template>
	<Teleport defer to=".svws-ui-header--actions">
		<svws-ui-button type="secondary" @click="print"><span class="icon i-ri-printer-line" />Drucken</svws-ui-button>
		<svws-ui-modal-hilfe> <hilfe-lehrer-stundenplan /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-flex-col overflow-x-auto">
		<template v-if="stundenplan() === undefined">
			<div class="flex flex-col gap-2 justify-center items-center min-h-full w-full grow text-headline-md text-ui-contrast-50 text-center">
				<span class="icon-xxl i-ri-calendar-event-line" />
				<span>Derzeit liegt kein Stundenplan<br>für diesen Lernabschnitt vor.</span>
			</div>
		</template>
		<template v-else>
			<stundenplan-auswahl :stundenplan="stundenplan()" :map-stundenplaene :goto-stundenplan :goto-wochentyp :goto-kalenderwoche :manager :wochentyp
				:kalenderwoche :ganzer-stundenplan :set-ganzer-stundenplan autofocus />
			<stundenplan-lehrer :id :ignore-empty :manager :wochentyp :kalenderwoche />
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { LehrerStundenplanProps } from './SLehrerStundenplanProps';

	defineProps<LehrerStundenplanProps>();

	const print = () => {
		window.print();
	};

</script>
