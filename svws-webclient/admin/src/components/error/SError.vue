<template>
	<svws-ui-app-layout>
		<template #sidebar>
			<svws-ui-menu>
				<svws-ui-menu-item :active="false" @click="goBack">
					<template #label>
						Zurück
					</template>
					<template #icon>
						<span class="icon-lg i-ri-arrow-go-back-line" />
					</template>
				</svws-ui-menu-item>
				<svws-ui-menu-item :active="false" @click="reloadClient">
					<template #label>
						Neu laden
					</template>
					<template #icon>
						<span class="icon-l i-ri-restart-line" />
					</template>
				</svws-ui-menu-item>
			</svws-ui-menu>
		</template>
		<template #main>
			<div class="app--page">
				<svws-ui-header>
					<div class="flex items-center">
						<div>
							<span class="inline-flex gap-2"><span class="icon i-ri-alert-fill icon-error" />{{ error?.name }}</span>
							<br>
							<span class="opacity-40">
								<template v-if="code !== undefined">
									Fehler {{ code }}
								</template>
								<template v-else>
									Unbekannter Fehlercode
								</template>
							</span>
						</div>
					</div>
				</svws-ui-header>
				<div class="router-tab-bar--area" v-if="error !== undefined">
					<div class="router-tab-bar--panel">
						<div class="page page-grid-cards">
							<svws-ui-content-card :title="error?.message">
								<pre>{{ error.stack }}</pre>
							</svws-ui-content-card>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>


<script setup lang="ts">

	import type { ErrorProps } from "./SErrorProps";

	const props = defineProps<ErrorProps>();

	function goBack() {
		window.history.back();
	}

	function reloadClient() {
		window.location.href = window.location.origin;
	}

</script>
