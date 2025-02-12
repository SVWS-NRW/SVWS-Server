<template>
	<div class="page--flex">
		<header class="svws-ui-header max-w-[140rem]">
			<div class="svws-ui-header--title gap-x-8 lg:gap-x-16 w-full">
				<div class="svws-headline-wrapper flex-[2]">
					<h2 class="svws-headline">
						<span>Konfiguration des SVWS-Servers</span>
					</h2>
					<span class="svws-subline">
						<span> Version: TODO </span>
					</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<div class="page page-grid-cards">
			<svws-ui-content-card title="Im Aufbau">
				Diese Seite befindet sich im Aufbau und wird Anpassungen an einigen Konfigurationseinstellungen des SVWS-Servers ermöglichen.
				<svws-ui-spacing :size="2" />
				<svws-ui-button type="primary" @click="downloadZertifikat"><span class="icon-sm i-ri-upload-2-line" />Zertifikat exportieren</svws-ui-button>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { ConfigAppProps } from "./SConfigAppProps";

	const props = defineProps<ConfigAppProps>();

	async function downloadZertifikat() {
		const { data, name } = await props.getCert();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}
</script>
