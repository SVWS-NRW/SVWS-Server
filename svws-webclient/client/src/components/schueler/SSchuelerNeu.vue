<template>
	<div class="page--content">
		<svws-ui-todo title="Schüler anlegen">
			Dieser Bereich ist noch in Entwicklung. Hier werden später alle initialen Informationen zur Anlage eines neuen Schülers eingepflegt.
		</svws-ui-todo>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { SchuelerStammdaten } from "@core";
	import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";

	const props = defineProps<SchuelerNeuProps>();

	const data = ref<Partial<SchuelerStammdaten>>({});

</script>
