<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-button type="secondary" @click="print"><span class="icon i-ri-printer-line" />Drucken</svws-ui-button>
		<svws-ui-modal-hilfe> <hilfe-schueler-stundenplan /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page--content page--content--flex h-full">
		<template v-if="stundenplan() === undefined">
			<div class="svws-ui-empty">
				<span class="icon i-ri-calendar-event-line" />
				<span>Derzeit liegt kein Stundenplan<br>für diesen Lernabschnitt vor.</span>
			</div>
		</template>
		<template v-else>
			<stundenplan-auswahl :stundenplan="stundenplan()" :map-stundenplaene :goto-stundenplan :goto-wochentyp :goto-kalenderwoche :manager :wochentyp :kalenderwoche :ganzer-stundenplan :set-ganzer-stundenplan
				autofocus />
			<stundenplan-schueler :id :ignore-empty :manager :wochentyp :kalenderwoche />
		</template>
	</div>
</template>

<script setup lang="ts">

	import { onMounted, ref } from "vue";
	import type { SchuelerStundenplanProps } from "./SchuelerStundenplanProps";

	defineProps<SchuelerStundenplanProps>();

	const print = () => {
		window.print();
	};

	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>
