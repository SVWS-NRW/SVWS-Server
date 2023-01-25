<template>
	<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Name der Schule" v-model="aufnehmdendSchulnummer" type="text" />
			<svws-ui-text-input placeholder="Wechseldatum" v-model="aufnehmdendWechseldatum" type="date" />
			<svws-ui-checkbox text="Aufnahme bestätigt" v-model="aufnehmdendBestaetigt" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { PropDataSchulbesuch } from "./PropDataSchulbesuch";

	const props = defineProps<{
		data: PropDataSchulbesuch;
	}>();

	const aufnehmdendSchulnummer = props.data.aufnehmdendSchulnummer();
	const aufnehmdendWechseldatum = props.data.aufnehmdendWechseldatum();
	const aufnehmdendBestaetigt = props.data.aufnehmdendBestaetigt();

</script>
