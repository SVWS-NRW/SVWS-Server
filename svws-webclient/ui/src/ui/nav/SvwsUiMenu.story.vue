<template>
	<Story title="Sidebar/Menu" id="svws-ui-menu" :layout="{ type: 'single', iframe: false }" group="app">
		<Variant title="Default" id="default">
			<div class="w-full max-w-[80px] mx-auto h-full">
				<svws-ui-menu>
					<template #header>
						<svws-ui-menu-header user="Max Mustermann" schule="Muster-Gymnasium" schema="DBName" />
					</template>
					<template #default>
						<svws-ui-menu-item>
							<template #icon><span class="icon-lg i-ri-school-line" /></template>
							<template #label>Schule</template>
						</svws-ui-menu-item>
						<svws-ui-menu-item active>
							<template #icon><span class="icon-lg i-ri-group-line" /></template>
							<template #label>Schüler</template>
						</svws-ui-menu-item>
						<svws-ui-menu-item>
							<template #icon><span class="icon-lg i-ri-briefcase-line" /></template>
							<template #label>Lehrkräfte</template>
						</svws-ui-menu-item>
					</template>
					<template #footer>
						<svws-ui-menu-item>
							<template #icon><span class="icon-lg i-ri-settings-3-line" /></template>
							<template #label>Einstellungen</template>
						</svws-ui-menu-item>
						<svws-ui-menu-item subline="">
							<template #icon><span class="icon-lg i-ri-logout-circle-line" /></template>
							<template #label>Abmelden</template>
						</svws-ui-menu-item>
					</template>
				</svws-ui-menu>
			</div>
		</Variant>
	</Story>
</template>
