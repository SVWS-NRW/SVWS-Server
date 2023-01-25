<template>
	<svws-ui-content-card title="Sekundarstufe I">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Jahr Wechsel Sek I" v-model="sekIWechsel" type="text" />
			<svws-ui-text-input placeholder="Erste Schulform Sek I" v-model="sekIErsteSchulform" type="text" />
			<svws-ui-text-input placeholder="Jahr Wechsel Sek II" v-model="sekIIWechsel" type="text" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { PropDataSchulbesuch } from "./PropDataSchulbesuch";

	const props = defineProps<{
		data: PropDataSchulbesuch;
	}>();

	const sekIWechsel = props.data.sekIWechsel();
	const sekIErsteSchulform = props.data.sekIErsteSchulform();
	const sekIIWechsel = props.data.sekIIWechsel();

</script>
