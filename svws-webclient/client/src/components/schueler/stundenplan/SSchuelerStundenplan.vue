<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<s-schueler-stundenplan-drucken-modal v-slot="{ openModal }" :map-stundenplaene :get-p-d-f :api-status>
			<svws-ui-button @click="openModal" type="secondary"><span class="icon i-ri-printer-line" /> Stundenplan drucken</svws-ui-button>
		</s-schueler-stundenplan-drucken-modal>
		<svws-ui-modal-hilfe> <hilfe-schueler-stundenplan /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-flex-col overflow-x-auto">
		<template v-if="stundenplan() === undefined">
			<div class="flex flex-col gap-2 justify-center items-center min-h-full w-full grow text-headline-md text-ui-50 text-center">
				<span class="icon-xxl i-ri-calendar-event-line" />
				<span>Derzeit liegt kein Stundenplan<br>für diesen Lernabschnitt vor.</span>
			</div>
		</template>
		<template v-else>
			<hr>
			<stundenplan-auswahl :stundenplan="stundenplan()" :map-stundenplaene :goto-stundenplan :goto-wochentyp :goto-kalenderwoche :manager :wochentyp
				:kalenderwoche :ganzer-stundenplan :set-ganzer-stundenplan autofocus />
			<stundenplan-schueler :id :ignore-empty :manager :wochentyp :kalenderwoche />
		</template>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerStundenplanProps } from "./SSchuelerStundenplanProps";

	defineProps<SchuelerStundenplanProps>();

</script>
