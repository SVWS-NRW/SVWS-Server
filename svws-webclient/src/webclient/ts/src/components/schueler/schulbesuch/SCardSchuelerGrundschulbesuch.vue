<template>
	<svws-ui-content-card title="Grundschulbesuch">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Einschulung" v-model="grundschuleEinschulungsjahr" type="text" />
			<svws-ui-text-input placeholder="Einschulungsart" v-model="grundschuleEinschulungsartID" />
			<svws-ui-text-input placeholder="EP-Jahre" v-model="grundschuleJahreEingangsphase" />
			<svws-ui-text-input placeholder="Übergangsempfehlung Jg. 5" v-model="grundschuleUebergangsempfehlungID" type="text" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { PropDataSchulbesuch } from "./PropDataSchulbesuch";

	const props = defineProps<{
		data: PropDataSchulbesuch;
	}>();

	const grundschuleEinschulungsjahr = props.data.grundschuleEinschulungsjahr();
	const grundschuleEinschulungsartID = props.data.grundschuleEinschulungsartID();
	const grundschuleJahreEingangsphase = props.data.grundschuleJahreEingangsphase();
	const grundschuleUebergangsempfehlungID = props.data.grundschuleUebergangsempfehlungID();

</script>
